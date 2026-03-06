package frc.robot.subsystems.shooter.targeting;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.shooter.targeting.TargetingConstants.ShootingParameters;
import frc.robot.subsystems.shooter.targeting.TargetingConstants.ShotSettings;
import frc.robot.subsystems.shooter.turret.TurretConstants;
import java.util.List;
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

  @AutoLogOutput public Pose2d leftFerryingPosition = TargetingConstants.blueLeftFerryingTarget;
  @AutoLogOutput public Pose2d rightFerryingPosition = TargetingConstants.blueRightFerryingTarget;

  @AutoLogOutput public boolean ferryingLeft = true;

  List<Pose2d> ferryingPositions;

  @AutoLogOutput public boolean isFerrying = false;
  CommandSwerveDrivetrain drivetrain;

  public TargetingSubsystem(CommandSwerveDrivetrain dt) {
    // hubPosition = new Pose2d(new Translation2d(11.909, 4.027), new Rotation2d());

    // hubPosition = new Pose2d(new Translation2d(11.909 - 0.0381, 4.027), new Rotation2d());
    hubPosition = TargetingConstants.blueHubPosition;

    if (DriverStation.getAlliance().isPresent()
        && DriverStation.getAlliance().get() == Alliance.Red) {
      hubPosition = TargetingConstants.redHubPosition;
      leftFerryingPosition = TargetingConstants.redLeftFerryingTarget;
      rightFerryingPosition = TargetingConstants.redRightFerryingTarget;
    }
    // ferryingPositions = List.of(leftFerryingPosition, rightFerryingPosition);

    // TargetingConstants.hubShotMap.put(
    //     2.1, new ShotSettings(0.0, Rotation2d.fromRotations(0.06665), 70.0));
    // TargetingConstants.hubShotMap.put(
    //     2.706, new ShotSettings(0.0, Rotation2d.fromRotations(0.065), 70.0));
    // TargetingConstants.hubShotMap.put(
    //     3.486, new ShotSettings(0.0, Rotation2d.fromRotations(0.0698), 75.0)); // 0.08
    // TargetingConstants.hubShotMap.put(
    //     4.135, new ShotSettings(0.0, Rotation2d.fromRotations(0.084229), 75.0));
    // TargetingConstants.hubShotMap.put(
    //     4.583, new ShotSettings(0.0, Rotation2d.fromRotations(0.091064), 80.0));
    // TargetingConstants.hubShotMap.put(
    //     5.122, new ShotSettings(0.0, Rotation2d.fromRotations(0.096), 80.0));

    TargetingConstants.hubShotMap.put(
        2.1, new ShotSettings(1.12, Rotation2d.fromRotations(0.0522), 70.0));
    TargetingConstants.hubShotMap.put(
        2.706, new ShotSettings(1.13, Rotation2d.fromRotations(0.063721), 70.0)); // found it
    TargetingConstants.hubShotMap.put(
        3.486, new ShotSettings(1.15, Rotation2d.fromRotations(0.075684), 75.0)); // found it
    TargetingConstants.hubShotMap.put(
        4.135, new ShotSettings(1.16, Rotation2d.fromRotations(0.084229), 75.0)); // found it
    TargetingConstants.hubShotMap.put(
        4.583, new ShotSettings(1.17, Rotation2d.fromRotations(0.091064), 80.0));
    TargetingConstants.hubShotMap.put(
        5.122, new ShotSettings(1.17, Rotation2d.fromRotations(0.096), 80.0));
    drivetrain = dt;
  }

  @Override
  public void periodic() {

    if (DriverStation.getAlliance().isPresent()
        && DriverStation.getAlliance().get() == Alliance.Red) {
      hubPosition = TargetingConstants.redHubPosition;
      leftFerryingPosition = TargetingConstants.redLeftFerryingTarget;
      rightFerryingPosition = TargetingConstants.redRightFerryingTarget;
    } else {
      hubPosition = TargetingConstants.blueHubPosition;
      leftFerryingPosition = TargetingConstants.blueLeftFerryingTarget;
      rightFerryingPosition = TargetingConstants.blueRightFerryingTarget;
    }
    Pose2d robotPose = drivetrain.getRobotPose();
    ChassisSpeeds fieldSpeeds = drivetrain.getFieldSpeeds();
    if (isFerrying) {
      Pose2d currentFerryingPos = rightFerryingPosition;
      if (ferryingLeft) {
        currentFerryingPos = leftFerryingPosition;
      }
      ShootingParameters ferryingParams =
          calculateShot(robotPose, fieldSpeeds, currentFerryingPos.getTranslation(), false);
      calculatedParams =
          new ShootingParameters(
              ferryingParams.turretAngle(),
              TargetingConstants.ferryingHoodAngle,
              TargetingConstants.ferryingRPS);
    } else {
      calculatedParams = calculateShot(robotPose, fieldSpeeds, hubPosition.getTranslation(), false);
    }
  }

  public ShootingParameters calculateShot(
      Pose2d robotPose, ChassisSpeeds fieldSpeeds, Translation2d targetPose, boolean SOTM) {

    Translation2d filteredRobotVelocity =
        new Translation2d(
            fieldSpeeds.vxMetersPerSecond,
            fieldSpeeds.vyMetersPerSecond);

    Translation2d futureTurretPos =
        robotPose
            .getTranslation()
            .plus(TurretConstants.turretOffset.rotateBy(robotPose.getRotation()))
    .plus(filteredRobotVelocity.times(TargetingConstants.estimatedShotLatency));

    Translation2d realDisplacementToHub = targetPose.minus(futureTurretPos);

    realDistance = realDisplacementToHub.getNorm();

    // System.out.println(realDistance);
    realDistance = MathUtil.clamp(realDistance, 2.1, 5.122);
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

    Translation2d aimingVector =
        realDisplacementToHub; // realDisplacementToHub.minus(futureTurretPos);

    Rotation2d robotRelativeTurretAngle = aimingVector.getAngle().minus(robotPose.getRotation());

    // double rots = robotRelativeTurretAngle.getRotations();
    // rots = Math.round(rots * Math.pow(10, 2)) / Math.pow(10, 2);

    ShotSettings mapValues = TargetingConstants.hubShotMap.get(realDistance);
    if (mapValues == null) {
      return calculatedParams; // Return last known good params
    }

    turretPos = new Pose2d(futureTurretPos, robotRelativeTurretAngle);

    targetTurretAngle = robotRelativeTurretAngle;
    targetHoodAngle = mapValues.hoodAngle();
    targetFlywheelRPS = mapValues.wheelRPS();
    return new ShootingParameters(
        robotRelativeTurretAngle, mapValues.hoodAngle(), Math.rint(mapValues.wheelRPS()));
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

  public void isFerrying(boolean x) {
    isFerrying = x;
  }
}
