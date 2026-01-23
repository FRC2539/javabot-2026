package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import edu.wpi.first.wpilibj2.command.Command;

public class ShootOnTheFlyCommand extends Command {
    
    public record ShotSettings(Double timeOfFlight, Double hoodAngle) implements Interpolatable<ShotSettings> {
        @Override
        public ShotSettings interpolate(ShotSettings endValue, double t) {
            return new ShotSettings(
                MathUtil.interpolate(this.timeOfFlight, endValue.timeOfFlight, t),
                MathUtil.interpolate(this.hoodAngle, endValue.hoodAngle, t)
            );
        }
    }

    // 0.1 second buffer, 0.1 / 0.02 (50hz) = 5, taken from 6328.
    private final LinearFilter vxFilter = LinearFilter.movingAverage(5);
    private final LinearFilter vyFilter = LinearFilter.movingAverage(5);

    private final InterpolatingTreeMap<Double, ShotSettings> shotMap = new InterpolatingTreeMap<>(
        InverseInterpolator.forDouble(), 
        ShotSettings::interpolate
    );

    private double latency = 0.020;
    private Translation2d robotPosition;
    private Translation2d hubPosition;
    private Translation2d robotVelocity;
    Translation2d filteredVelocity;
    
    public ShootOnTheFlyCommand() {

    }

    @Override
    public void initialize() { 
        
    }

    @Override
    public void execute() { 
        
        /* pass our robot velocity through a linear filter to smooth out encoder noise and hopefully reduce turret/hoot jitter. */
        double smoothedVx = vxFilter.calculate(robotVelocity.getX());
        double smoothedVy = vyFilter.calculate(robotVelocity.getY());
        filteredVelocity = new Translation2d(smoothedVx, smoothedVy);

        Translation2d futurePosition = robotPosition.plus(filteredVelocity.times(latency));
        Translation2d realDisplacementToHub = hubPosition.minus(futurePosition);

        double realDistance = realDisplacementToHub.getNorm();
        double estimatedFlightTime = shotMap.get(realDistance).timeOfFlight;

        /* 
            * We cannot calculate the shot in a single pass because the Final Distance and 
            * Time of Flight (ToF) are dependent on each other.
            * 1. To know where the target will be, we need the ToF.
            * 2. To know the ToF, we need the Final Distance from our Look-Up Table.
            * 3. To know the Final Distance, we need to know where we'll be when the ball arrives (ToF).
            *
            * This is a problem, if we only do one pass, 
            * we will undershoot (if moving away) or overshoot (if moving toward) 
            * because we aren't accounting for how the flight time changes as we move.
            *
            * By using a for loop we can converge on the right flight time and distance pair that gets us the speed we need.
        */

        for (int i = 0; i < 7; i++) { // 7 iterations

            // Where will the hub be relative to us when the ball arrives?
            // (Subtracting velocity because if we move forward, hub effectively moves toward us)
            Translation2d predictedDisplacement = realDisplacementToHub.minus(filteredVelocity.times(estimatedFlightTime));
            
            double predictedDistance = predictedDisplacement.getNorm();
            
            // Look up the new flight time for this hypothetical distance
            double newFlightTime = shotMap.get(predictedDistance).timeOfFlight;
            
            // Have we converged?
            if (Math.abs(newFlightTime - estimatedFlightTime) < 0.02) {
                estimatedFlightTime = newFlightTime;
                break; 
            }
            estimatedFlightTime = newFlightTime;
        }

        double finalDistance = realDisplacementToHub.minus(filteredVelocity.times(estimatedFlightTime)).getNorm();
        Translation2d targetDirection = realDisplacementToHub.minus(filteredVelocity.times(estimatedFlightTime)).div(finalDistance);

        ShotSettings targetSettings = shotMap.get(finalDistance); // hood angle!

        Rotation2d angleNeeded = targetDirection.getAngle(); // this minus our robot's current heading is the angle the *turret* needs to rotate to.

        // Rotation2d turretLocalAngle = targetFieldAngle.minus(robotRotation);
    }

}