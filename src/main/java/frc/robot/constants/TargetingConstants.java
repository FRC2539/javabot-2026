package frc.robot.constants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;
import java.util.List;

public class TargetingConstants {
  public static double estimatedShotLatency = 0.04;

  public static Translation2d leftFerryingTarget = new Translation2d();
  public static Translation2d rightFerryingTarget = new Translation2d();
  public static List<Translation2d> ferryingTargets =
      List.of(leftFerryingTarget, rightFerryingTarget);

  public static final InterpolatingTreeMap<Double, ShotSettings> hubShotMap =
      new InterpolatingTreeMap<>(InverseInterpolator.forDouble(), ShotSettings::interpolate);

  public static record ShotSettings(Double timeOfFlight, Rotation2d hoodAngle, Double wheelRPM)
      implements Interpolatable<ShotSettings> {
    @Override
    public ShotSettings interpolate(ShotSettings endValue, double t) {
      return new ShotSettings(
          MathUtil.interpolate(this.timeOfFlight, endValue.timeOfFlight, t),
          Rotation2d.fromDegrees(
              MathUtil.interpolate(
                  this.hoodAngle.getDegrees(), endValue.hoodAngle.getDegrees(), t)),
          MathUtil.interpolate(this.wheelRPM, endValue.wheelRPM, t));
    }
  }

  public record ShootingParameters(
      Rotation2d turretAngle, Rotation2d hoodAngle, double flywheelRPM) {}
}
