package frc.robot.subsystems.shooter.targeting;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.shooter.targeting.TargetingConstants.ShootingParameters;
import frc.robot.subsystems.shooter.targeting.TargetingConstants.ShotSettings;
import frc.robot.subsystems.shooter.turret.TurretConstants;
import java.util.function.Supplier;
import org.littletonrobotics.junction.AutoLogOutput;

public class TargetingSubsystem extends SubsystemBase {
  private ShootingParameters calculatedParams =
      new ShootingParameters(new Rotation2d(), new Rotation2d(), 0.0);

  // private static final LinearFilter vxFilter = LinearFilter.movingAverage(5);
  // private static final LinearFilter vyFilter = LinearFilter.movingAverage(5);

  @AutoLogOutput public Pose2d hubPosition;
  @AutoLogOutput public Pose2d turretPos;
  @AutoLogOutput public double realDistance = 0;

  @AutoLogOutput public Rotation2d targetTurretAngle;
  @AutoLogOutput public Rotation2d targetHoodAngle;
  @AutoLogOutput public double targetFlywheelRPS;

  @AutoLogOutput public double turretPaddingDeg = 0.0;
  @AutoLogOutput public double distancePaddingMeters = 0.0;

  boolean isFerrying = false;
  CommandSwerveDrivetrain drivetrain;

  public TargetingSubsystem(CommandSwerveDrivetrain dt) {
    // hubPosition = AllianceFlipUtil.apply(FieldConstants.Hub.innerCenterPoint.toTranslation2d());
    // hubPosition = FieldConstants.Hub.innerCenterPoint.toTranslation2d();

    // TargetingConstants.hubShotMap.put(0, new ShotSettings(0.0, Rotation2d.fromRotations(0), 0));

    // TargetingConstants.hubShotMap.put(
    //     2.1, new ShotSettings(0.0, Rotation2d.fromRotations(0.052002), 75.0));
    // TargetingConstants.hubShotMap.put(
    //     2.78, new ShotSettings(0.0, Rotation2d.fromRotations(0.064697), 75.0));
    // TargetingConstants.hubShotMap.put(
    //     3.237, new ShotSettings(0.0, Rotation2d.fromRotations(0.073468), 75.0));
    // TargetingConstants.hubShotMap.put(
    //     4.52, new ShotSettings(0.0, Rotation2d.fromRotations(0.0964), 75.0));
    // TargetingConstants.hubShotMap.put(
    //     4.607, new ShotSettings(0.0, Rotation2d.fromRotations(0.08231), 75.0));
    // TargetingConstants.hubShotMap.put(
    //     5.46, new ShotSettings(0.0, Rotation2d.fromRotations(0.095215), 75.0));

    TargetingConstants.hubShotMap.put(
        2.1, new ShotSettings(0.0, Rotation2d.fromRotations(0.04248), 70.0));
    TargetingConstants.hubShotMap.put(
        2.706, new ShotSettings(0.0, Rotation2d.fromRotations(0.063721), 70.0));
    TargetingConstants.hubShotMap.put(
        3.486, new ShotSettings(0.0, Rotation2d.fromRotations(0.080780), 75.0));
    TargetingConstants.hubShotMap.put(
        4.135, new ShotSettings(0.0, Rotation2d.fromRotations(0.084229), 75.0));
    TargetingConstants.hubShotMap.put(
        4.583, new ShotSettings(0.0, Rotation2d.fromRotations(0.091064), 80.0));
    TargetingConstants.hubShotMap.put(
        5.122, new ShotSettings(0.0, Rotation2d.fromRotations(0.096), 80.0));
    hubPosition = new Pose2d(new Translation2d(11.909, 4.027), new Rotation2d());
    drivetrain = dt;
  }

  @Override
  public void periodic() {
    edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(
        "Targeting/Turret Padding", turretPaddingDeg);
    edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(
        "Targeting/Dist Padding", distancePaddingMeters);

    Pose2d robotPose = drivetrain.getRobotPose();
    ChassisSpeeds fieldSpeeds = drivetrain.getFieldSpeeds();

    // Translation2d target = isInAllianceZone(robotPose) ? hubPosition :
    // getFerryingTarget(robotPose);

    calculatedParams = calculateShot(robotPose, fieldSpeeds, hubPosition.getTranslation(), false);
  }

  public Command resetPadding() {
    return runOnce(
        () -> {
          turretPaddingDeg = 0.0;
          distancePaddingMeters = 0.0;
        });
  }

  public Command padtargetleft() {
    return runOnce(() -> turretPaddingDeg += 0.5);
  }

  public Command padtargetright() {
    return runOnce(() -> turretPaddingDeg -= 0.5);
  }

  public Command padtargetdeep() {
    return runOnce(() -> distancePaddingMeters += 0.1); // Distance is usually meters
  }

  public Command padtargetclose() {
    return runOnce(() -> distancePaddingMeters -= 0.1);
  }

  public ShootingParameters calculateShot(
      Pose2d robotPose, ChassisSpeeds fieldSpeeds, Translation2d targetPose, boolean SOTM) {

    // Translation2d filteredRobotVelocity =
    //     new Translation2d(
    //         vxFilter.calculate(fieldSpeeds.vxMetersPerSecond),
    //         vyFilter.calculate(fieldSpeeds.vyMetersPerSecond));

    Translation2d futureTurretPos =
        robotPose
            .getTranslation()
            .plus(TurretConstants.turretOffset.rotateBy(robotPose.getRotation()));
    // .plus(filteredRobotVelocity.times(TargetingConstants.estimatedShotLatency));

    Translation2d realDisplacementToHub = targetPose.minus(futureTurretPos);

    realDistance = realDisplacementToHub.getNorm();

    // System.out.println(realDistance);
    realDistance = MathUtil.clamp(realDistance, 2.1, 5.122);
    // double estimatedFlightTime = TargetingConstants.hubShotMap.get(realDistance).timeOfFlight();

    // Translation2d virtualTarget = targetPose;

    // double virtualDistance = realDistance;

    // if (SOTM) { // Shooting on the move!
    //   for (int i = 0; i < 5; i++) {

    //     virtualTarget = targetPose.minus(filteredRobotVelocity.times(estimatedFlightTime));

    //     virtualDistance = futureTurretPos.getDistance(virtualTarget);

    //     double newFlightTime = 0;//
    // TargetingConstants.hubShotMap.get(virtualDistance).timeOfFlight();

    //     if (Math.abs(newFlightTime - estimatedFlightTime) < 0.03) break;
    //     estimatedFlightTime = newFlightTime;
    //   }
    // }

    Translation2d aimingVector =
        realDisplacementToHub; // realDisplacementToHub.minus(futureTurretPos);

    Rotation2d robotRelativeTurretAngle = aimingVector.getAngle().minus(robotPose.getRotation());

    // // double rots = robotRelativeTurretAngle.getRotations();
    // // rots = Math.round(rots * Math.pow(10, 2)) / Math.pow(10, 2);

    // ShotSettings mapValues = TargetingConstants.hubShotMap.get(realDistance);
    // if (mapValues == null) {
    //   return calculatedParams; // Return last known good params
    // }

    // turretPos = new Pose2d(futureTurretPos, robotRelativeTurretAngle);

    // targetTurretAngle = robotRelativeTurretAngle;
    // targetHoodAngle = mapValues.hoodAngle();
    // targetFlywheelRPS = mapValues.wheelRPS();
    // return new ShootingParameters(
    //     robotRelativeTurretAngle, mapValues.hoodAngle(), Math.rint(mapValues.wheelRPS()));

    double paddedDistance = MathUtil.clamp(realDistance + distancePaddingMeters, 2.1, 5.122);
    ShotSettings mapValues = TargetingConstants.hubShotMap.get(paddedDistance);

    if (mapValues == null) {
      return calculatedParams;
    }

    Rotation2d paddedTurretAngle =
        robotRelativeTurretAngle.plus(Rotation2d.fromDegrees(turretPaddingDeg));

    turretPos = new Pose2d(futureTurretPos, paddedTurretAngle);

    targetTurretAngle = paddedTurretAngle;
    targetHoodAngle = mapValues.hoodAngle();
    targetFlywheelRPS = mapValues.wheelRPS();

    calculatedParams =
        new ShootingParameters(targetTurretAngle, targetHoodAngle, Math.rint(targetFlywheelRPS));

    return calculatedParams;
  }

  public Supplier<Rotation2d> getIdealTurretAngle() {
    return () -> calculatedParams.turretAngle();
  }

  public Supplier<Rotation2d> getIdealHoodAngle() {
    return () -> calculatedParams.hoodAngle();
  }

  public Supplier<Double> getIdealFlywheelRPS() {
    return () -> calculatedParams.flywheelRPS();
  }

  // public static boolean isInAllianceZone(Pose2d robotPose) {
  //   double boundaryLine = AllianceFlipUtil.applyX(FieldConstants.LinesVertical.allianceZone);

  //   return DriverStation.getAlliance().get() == Alliance.Blue
  //       ? (robotPose.getX() < boundaryLine)
  //       : (robotPose.getX() > boundaryLine);
  // }

  // public static Translation2d getFerryingTarget(Pose2d robotPose) {
  //   return AllianceFlipUtil.apply(
  //       robotPose.getTranslation().nearest(TargetingConstants.ferryingTargets));
  // }
}
