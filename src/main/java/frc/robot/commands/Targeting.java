package frc.robot.commands;

import java.util.List;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;

public class Targeting extends Command {


    public static InterpolatingDoubleTreeMap DistanceHoodMap = 
        new InterpolatingDoubleTreeMap();

    public static InterpolatingDoubleTreeMap DistanceTOFMap = 
        new InterpolatingDoubleTreeMap();

    public static final List<Double> distKeys = List.of(2.0, 4.0, 6.0, 8.0, 10.0);


    private double latency = 0.040;
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
        Translation2d futurePosition = robotPosition.plus(
            robotVelocity.times(latency)
        );

        // Find the vector pointing from our (future) self to the center of the Hub.
        Translation2d realDisplacementToHub = hubPosition.minus(futurePosition);

        // straight-line distance (hypotenuse) to the target.
        double realDistance = realDisplacementToHub.getNorm(); 

        // Its a unit vector now yippee
        Translation2d targetDirection = realDisplacementToHub.div(realDistance);

        double estimatedFlightTime = DistanceTOFMap.get(realDistance);

        double baselineSpeed = realDistance / estimatedFlightTime;

        // This would be our "true" neededVelocity assuming we were stationary.
        Translation2d targetVelocity = targetDirection.times(baselineSpeed);

        // Since the ball inherits our robot's current velocity, we subtract our velocity from the target velocity
        Translation2d neededVelocity = targetVelocity.minus(robotVelocity);
        // /*

        // * Convert the required shooter-relative velocity back into a "Virtual Distance."
        // * By finding the distance that (roughly) corresponds to this specific velocity in our 
        // * Distance -> Speed model (technically Distance -> TOF), we can reuse our existing (stationary) lookup 
        // * table for Hood Angle even though we're moving.
        // */

        for (Double dist : distKeys) {
            double tof = DistanceTOFMap.get(dist);

            double speed = dist / tof;

            if (speed >= neededVelocity.getNorm()) {
                estimatedIdealHoodAngle = DistanceHoodMap.get(dist);
            }
        }   
    }

    @Override
    public boolean isFinished() { // the result of this function decides if the command ends
        return false;
    }
}
