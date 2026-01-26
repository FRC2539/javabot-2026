package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import edu.wpi.first.wpilibj2.command.Command;

/* Shooting on the move is really just a vector problem, where from a strictly XY perspective V_ball = V_shooter + V_robot,
 * where we're trying to solve for V_shooter (the XY velocity imparted onto the ball that we can control indirectly through hood angle / wheel RPM)
*/
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
    private Rotation2d robotRotation;
    Translation2d filteredVelocity;
    
    public ShootOnTheFlyCommand() {

    }

    @Override
    public void initialize() { 
        
    }

    @Override
    public void execute() { 
        
        /* pass our robot velocity through a linear filter to smooth out encoder noise and hopefully reduce turret/hoot jitter, inspired by 6328 */
        double smoothedVx = vxFilter.calculate(robotVelocity.getX());
        double smoothedVy = vyFilter.calculate(robotVelocity.getY());
        filteredVelocity = new Translation2d(smoothedVx, smoothedVy);

        Translation2d futureRobotPos = robotPosition.plus(filteredVelocity.times(latency));
        Translation2d realDisplacementToHub = hubPosition.minus(futureRobotPos);

        double realDistance = realDisplacementToHub.getNorm();
        double estimatedFlightTime = shotMap.get(realDistance).timeOfFlight;

        /* When we're moving, the ball inherits the robot's speed when we shoot (relative to the field).
         * If we plug our *actual* distance from the hub (realDistance) into our shot map, 
         * we'll miss because our robot's velocity has changed the ball's trajectory.
         * The fundamental problem here is that our [Distance -> Hood Angle] map assumes that our robot is *stationary* when we shoot. 
         * To fix this, we calculate a virtual distance, a point in space where if the robot were STATIONARY, 
         * shooting would result in the ball landing in the actual hub. This problem is a circular dependency, 
         * to find the Virtual Distance, we need the ball's "real" TOF (using our current "real" robot pos & velocity),
         * But since we're moving, we can't use our [Distance -> ToF] map.
         * This loop below converges on a virtual distance that when plugged into our shotMap, 
         * gives us an adjusted hood angle that cancels out our robot's field velocity and leaves us with the "correct" shot to the hub.
        */

        /* adapted from 1690's software presentation from 2024, the equation for this virtual distance can't be solved algebraically and has to be approximated.
         * https://www.youtube.com/watch?v=vUtVXz7ebEE&
        */

        Translation2d virtualTarget = hubPosition;
        double virtualDistance = realDistance;
        for (int i = 0; i < 7; i++) { // 7 iterations
            virtualTarget = hubPosition.minus(filteredVelocity.times(estimatedFlightTime));

            virtualDistance = futureRobotPos.getDistance(virtualTarget);

            double newFlightTime = shotMap.get(virtualDistance).timeOfFlight;

            if (Math.abs(newFlightTime - estimatedFlightTime) < 0.02) break;
            estimatedFlightTime = newFlightTime;
        }

        Translation2d aimingVector = virtualTarget.minus(futureRobotPos);

        Rotation2d fieldRelativeTurretAngle = aimingVector.getAngle();

        Rotation2d robotRelativeTurretAngle = fieldRelativeTurretAngle.minus(robotRotation);

        double neededHoodAngle = shotMap.get(virtualDistance).hoodAngle;
    }

}