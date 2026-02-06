package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class TurretSolver {

  public static double flightTime = 0.0;
  public static double latency = 0.020;

  public static double solveTurretAngle(
      Pose2d hubPose,
      Pose2d robotPose,
      ChassisSpeeds fieldSpeeds) {

    Pose2d predictedRobotPose =
        new Pose2d(
            robotPose.getX() + fieldSpeeds.vxMetersPerSecond * latency,
            robotPose.getY() + fieldSpeeds.vyMetersPerSecond * latency,
            robotPose
                .getRotation()
                .plus(Rotation2d.fromRadians(fieldSpeeds.omegaRadiansPerSecond * latency)));

    double adjustedX = hubPose.getX() - fieldSpeeds.vxMetersPerSecond * flightTime;
    double adjustedY = hubPose.getY() - fieldSpeeds.vyMetersPerSecond * flightTime;

    Rotation2d fieldAngle =
        new Pose2d(adjustedX, adjustedY, new Rotation2d())
            .minus(predictedRobotPose)
            .getTranslation()
            .getAngle();

    double turretAngle =
        MathUtil.inputModulus(
            fieldAngle.minus(predictedRobotPose.getRotation()).getRadians(),
            -Math.PI,
            Math.PI);

    return turretAngle;
  }
}
