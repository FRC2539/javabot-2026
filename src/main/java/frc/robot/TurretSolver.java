package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class TurretSolver {
    
    public static double flightTime = 0;
    public static double latency = 0.020; // 20 ms?

    public static double solveTurretAngle(Pose2d hubPose, Pose2d currentRobotPose, ChassisSpeeds fieldSpeeds) {

        // This may be negligible, especially because we aren't moving *that* fast.
        Pose2d predictedRobotPose = new Pose2d(
            currentRobotPose.getX() + (fieldSpeeds.vxMetersPerSecond * latency),
            currentRobotPose.getY() + (fieldSpeeds.vyMetersPerSecond * latency),
            currentRobotPose.getRotation().plus(Rotation2d.fromRadians(fieldSpeeds.omegaRadiansPerSecond * latency))
        );

        /* When we shoot, the X and Y velocities of our robot are imparted onto the ball. 
        Since we know these velocities as well as an estimate for how long the ball will be in the air (timeOfFlight), 
        we can offset hubPose by the estimated X & Y distances the ball will travel while in the air.
        */
        double adjustedX = hubPose.getX() - (fieldSpeeds.vxMetersPerSecond * flightTime);
        double adjustedY = hubPose.getY() - (fieldSpeeds.vyMetersPerSecond * flightTime);
        Pose2d virtualTargetPose = new Pose2d(adjustedX, adjustedY, hubPose.getRotation());

        // This would be the angle our *robot* would need to be at to face the hub.

        Rotation2d fieldRelativeTargetAngle = virtualTargetPose.minus(virtualTargetPose).getRotation();
        
        Rotation2d targetTurretAngle = fieldRelativeTargetAngle.minus(predictedRobotPose.getRotation());

        double optimizedAngle = MathUtil.inputModulus(targetTurretAngle.getDegrees(), -180, 180);

        return optimizedAngle;
    }
}
