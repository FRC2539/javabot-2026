package frc.robot.subsystems.shooter.targeting;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.shooter.hood.HoodConstants;

public class TargetingConstants {
  public static double estimatedShotLatency = 0.04;
  public static double fieldLengthMeters = 16.54098798984;
  public static Rotation2d ferryingHoodAngle = HoodConstants.maxHoodAngle;
  public static double ferryingRPS = 80;
  public static Pose2d blueHubPosition = new Pose2d(4.3647,4.0345, Rotation2d.kZero);

  public static Pose2d leftFerryingTarget = new Pose2d(Units.inchesToMeters(40), Units.inchesToMeters(40), Rotation2d.kZero);
  public static Pose2d rightFerryingTarget = new Pose2d(Units.inchesToMeters(40), Units.inchesToMeters(40), Rotation2d.kZero);

  public static final InterpolatingTreeMap<Double, ShotSettings> hubShotMap =
      new InterpolatingTreeMap<>(InverseInterpolator.forDouble(), ShotSettings::interpolate);

  public static record ShotSettings(Double timeOfFlight, Rotation2d hoodAngle, Double wheelRPS)
      implements Interpolatable<ShotSettings> {
    @Override
    public ShotSettings interpolate(ShotSettings endValue, double t) {
      return new ShotSettings(
          MathUtil.interpolate(this.timeOfFlight, endValue.timeOfFlight, t),
          Rotation2d.fromDegrees(
              MathUtil.interpolate(
                  this.hoodAngle.getDegrees(), endValue.hoodAngle.getDegrees(), t)),
          MathUtil.interpolate(this.wheelRPS, endValue.wheelRPS, t));
    }
  }

  public record ShootingParameters(
      Rotation2d turretAngle, Rotation2d hoodAngle, double flywheelRPS) {}
}
