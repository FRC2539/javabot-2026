package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import edu.wpi.first.wpilibj2.command.Command;

public class Targeting extends Command {
    
    public record ShotSettings(Double timeOfFlight, Double hoodAngle) implements Interpolatable<ShotSettings> {
        @Override
        public ShotSettings interpolate(ShotSettings endValue, double t) {
            return new ShotSettings(
                MathUtil.interpolate(this.timeOfFlight, endValue.timeOfFlight, t),
                MathUtil.interpolate(this.hoodAngle, endValue.hoodAngle, t)
            );
        }
    }

    // 6.75 in (26)
    // 8.25 ()

    private final InterpolatingTreeMap<Double, ShotSettings> shotMap = new InterpolatingTreeMap<>(
        InverseInterpolator.forDouble(), 
        ShotSettings::interpolate
    );

    private double latency = 0.020;
    private Translation2d robotPosition;
    private Translation2d hubPosition;
    private Translation2d robotVelocity;
    
    public Targeting() {

    }

    @Override
    public void initialize() { 
        
    }

    @Override
    public void execute() { 
        Translation2d futurePosition = robotPosition.plus(robotVelocity.times(latency));
        Translation2d realDisplacementToHub = hubPosition.minus(futurePosition);

        double realDistance = realDisplacementToHub.getNorm();
        double estimatedFlightTime = shotMap.get(realDistance).timeOfFlight;

        /* 
            * We cannot calculate the shot in a single pass because the "Final Distance" and 
            * "Time of Flight" (ToF) are codependent. 
            * * 1. To know where the target will be, we need the ToF.
            * 2. To know the ToF, we need the Final Distance from our Look-Up Table.
            * 3. To know the Final Distance, we need to know where we'll be when the ball arrives (ToF).
            *
            * This circular dependency creates a moving target. If we only do one pass, 
            * we will consistently undershoot (if moving away) or overshoot (if moving toward) 
            * because we aren't accounting for how the flight time changes as we move.
            *
            * By iterating 3-5 times, the Time of Flight and Predicted Distance "converge" 
            * on a single stable solution where the physics of the ball and the motion of 
            * the robot agree.
        */
        
        for (int i = 0; i < 7; i++) { // 7 iterations

            // Where will the hub be relative to us when the ball arrives?
            // (Subtracting velocity because if we move forward, hub effectively moves toward us)
            Translation2d predictedDisplacement = realDisplacementToHub.minus(robotVelocity.times(estimatedFlightTime));
            
            double predictedDistance = predictedDisplacement.getNorm();
            
            // Look up the NEW flight time for this predicted distance
            double newFlightTime = shotMap.get(predictedDistance).timeOfFlight;
            
            // Check if it's converged
            if (Math.abs(newFlightTime - estimatedFlightTime) < 0.02) {
                estimatedFlightTime = newFlightTime;
                break; 
            }
            estimatedFlightTime = newFlightTime;
        }

        double finalDistance = realDisplacementToHub.minus(robotVelocity.times(estimatedFlightTime)).getNorm();
        Translation2d targetDirection = realDisplacementToHub.minus(robotVelocity.times(estimatedFlightTime)).div(finalDistance);

        double baselineSpeed = finalDistance / estimatedFlightTime;
        Translation2d targetVelocity = targetDirection.times(baselineSpeed);

        // This is the *extra* XY velocity that we need to impart on the ball (either via flywheel speed, hood angle, or both) to land in the hub.
        //Translation2d neededVelocity = targetVelocity.minus(robotVelocity);   

        ShotSettings targetSettings = shotMap.get(finalDistance);

    }

    @Override
    public boolean isFinished() { // the result of this function decides if the command ends
        return false;
    }
}