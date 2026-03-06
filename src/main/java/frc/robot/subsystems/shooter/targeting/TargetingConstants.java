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
  public static Rotation2d ferryingHoodAngle = Rotation2d.fromRotations(HoodConstants.maxHoodAngle.getRotations() - 0.03);
  public static double ferryingRPS = 70;
  public static Pose2d blueHubPosition = new Pose2d(4.6255,4.0345, Rotation2d.kZero); // 4.3647, 4.0345

  public static Pose2d redHubPosition = new Pose2d(TargetingConstants.fieldLengthMeters - blueHubPosition.getX(), blueHubPosition.getY(), Rotation2d.kZero);

  public static Pose2d blueLeftFerryingTarget = new Pose2d(0, 3.5, Rotation2d.kZero);
  public static Pose2d blueRightFerryingTarget = new Pose2d(0, 7, Rotation2d.kZero);
  public static Pose2d redLeftFerryingTarget = new Pose2d(TargetingConstants.fieldLengthMeters - blueLeftFerryingTarget.getX(), blueLeftFerryingTarget.getY(), Rotation2d.kZero);
  public static Pose2d redRightFerryingTarget = new Pose2d(TargetingConstants.fieldLengthMeters - blueRightFerryingTarget.getX(), blueRightFerryingTarget.getY(), Rotation2d.kZero);

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
