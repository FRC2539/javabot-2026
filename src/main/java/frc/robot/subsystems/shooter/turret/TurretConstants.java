package frc.robot.subsystems.shooter.turret;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public final class TurretConstants {

  private TurretConstants() {}

  public static final int turretMotorId = 17;
  public static final int turretEncoderID = 40;
  public static final String canBus = "rio";

  // Limits
  public static final Rotation2d minAngle = Rotation2d.fromDegrees(-180);
  public static final Rotation2d maxAngle = Rotation2d.fromDegrees(180);
  public static final Rotation2d realZeroOffset =
      Rotation2d.fromDegrees(
          30); // The angle offset from "ideal" 0 (facing the intake) to the actual middle value of
  // the turret's range.

  public static final double maxAngleRot = Units.degreesToRotations(180);

  public static final Rotation2d setpointTolerance = Rotation2d.fromDegrees(1.5);

  public static final double maxVelRotPerSec = Units.radiansToRotations(8.0);

  public static final double maxAccelRotPerSec2 = Units.radiansToRotations(30.0);

  public static final Translation2d turretOffset = new Translation2d(-0.127, 0.0);

  public static final FeedbackConfigs feedbackConfig =
      new FeedbackConfigs()
          .withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)
          .withFeedbackRemoteSensorID(turretEncoderID)
          .withRotorToSensorRatio(0)
          .withSensorToMechanismRatio(0);

  public static final MotionMagicConfigs motionMagicConfig =
      new MotionMagicConfigs()
          .withMotionMagicCruiseVelocity(maxVelRotPerSec)
          .withMotionMagicAcceleration(maxAccelRotPerSec2)
          .withMotionMagicJerk(0);

  public static final TalonFXConfiguration turretMotorConfig =
      new TalonFXConfiguration().withFeedback(feedbackConfig).withMotionMagic(motionMagicConfig);
}
