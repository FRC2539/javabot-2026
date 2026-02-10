package frc.robot.subsystems.shooter;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.FieldConstants;
import frc.robot.constants.TargetingConstants;
import frc.robot.constants.TargetingConstants.ShootingParameters;
import frc.robot.constants.TargetingConstants.ShotSettings;
import frc.robot.constants.TurretConstants;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.util.AllianceFlipUtil;

public class TargetingSubsystem extends SubsystemBase {
  private ShootingParameters calculatedParams;

  private static final LinearFilter vxFilter = LinearFilter.movingAverage(5);
  private static final LinearFilter vyFilter = LinearFilter.movingAverage(5);

  private Translation2d hubPosition;

  boolean isFerrying = false;
  CommandSwerveDrivetrain drivetrain;

  public TargetingSubsystem() {
    hubPosition = AllianceFlipUtil.apply(FieldConstants.Hub.innerCenterPoint.toTranslation2d());
  }

  @Override
  public void periodic() {
    Pose2d robotPose = drivetrain.getRobotPose();
    ChassisSpeeds fieldSpeeds = drivetrain.getFieldRelativeChassisSpeeds();

    Translation2d target = isInAllianceZone(robotPose) ? hubPosition : getFerryingTarget(robotPose);

    calculatedParams = calculateShot(robotPose, fieldSpeeds, target, true);
  }

  public static ShootingParameters calculateShot(
      Pose2d robotPose, ChassisSpeeds fieldSpeeds, Translation2d targetPose, boolean SOTM) {

    Translation2d filteredRobotVelocity =
        new Translation2d(
            vxFilter.calculate(fieldSpeeds.vxMetersPerSecond),
            vyFilter.calculate(fieldSpeeds.vyMetersPerSecond));

    Translation2d futureTurretPos =
        robotPose
            .getTranslation()
            .plus(TurretConstants.turretOffset.rotateBy(robotPose.getRotation()))
            .plus(filteredRobotVelocity.times(TargetingConstants.estimatedShotLatency));

    Translation2d realDisplacementToHub = targetPose.minus(futureTurretPos);

    double realDistance = realDisplacementToHub.getNorm();
    double estimatedFlightTime = TargetingConstants.hubShotMap.get(realDistance).timeOfFlight();

    Translation2d virtualTarget = targetPose;

    double virtualDistance = realDistance;

    if (SOTM) { // Shooting on the move!
      for (int i = 0; i < 5; i++) {
        virtualTarget = targetPose.minus(filteredRobotVelocity.times(estimatedFlightTime));

        virtualDistance = futureTurretPos.getDistance(virtualTarget);

        double newFlightTime = TargetingConstants.hubShotMap.get(virtualDistance).timeOfFlight();

        if (Math.abs(newFlightTime - estimatedFlightTime) < 0.03) break;
        estimatedFlightTime = newFlightTime;
      }
    }

    Translation2d aimingVector = virtualTarget.minus(futureTurretPos);

    Rotation2d robotRelativeTurretAngle = aimingVector.getAngle().minus(robotPose.getRotation());

    ShotSettings mapValues = TargetingConstants.hubShotMap.get(virtualDistance);

    return new ShootingParameters(
        robotRelativeTurretAngle, mapValues.hoodAngle(), mapValues.wheelRPM());
  }

  public Rotation2d getIdealTurretAngle() {
    return calculatedParams.turretAngle();
  }

  public Rotation2d getIdealHoodAngle() {
    return calculatedParams.hoodAngle();
  }

  public double getIdealFlywheelRPM() {
    return calculatedParams.flywheelRPM();
  }

  public static boolean isInAllianceZone(Pose2d robotPose) {
    double boundaryLine = AllianceFlipUtil.applyX(FieldConstants.LinesVertical.allianceZone);

    return DriverStation.getAlliance().get() == Alliance.Blue
        ? (robotPose.getX() < boundaryLine)
        : (robotPose.getX() > boundaryLine);
  }

  public static Translation2d getFerryingTarget(Pose2d robotPose) {
    return AllianceFlipUtil.apply(robotPose.getTranslation().nearest(TargetingConstants.ferryingTargets));
  }
}
