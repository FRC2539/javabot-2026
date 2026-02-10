package frc.robot.subsystems.shooter.turret;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public final class TurretConstants {

  // CAN
  public static final int turretMotorId = 20;
  public static final int turretEncoderID = 21;
  public static final String canBus = "rio";

  // Limits
  public static final double minAngleRot = Units.degreesToRotations(-180);
  public static final double maxAngleRot = Units.degreesToRotations(180);

  // Motion Magic
  public static final double maxVelRadPerSec = 8.0;
  public static final double maxAccelRadPerSec2 = 30.0;

  public static final double setpointToleranceRot = Units.degreesToRotations(1.5);

  public static final Translation2d turretOffset = new Translation2d(-0.127, 0.0);

  // Motor Configs

  public static final FeedbackConfigs feedbackConfig =
      new FeedbackConfigs()
          .withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)
          .withFeedbackRemoteSensorID(turretEncoderID);

  public static final MotionMagicConfigs motionMagicConfig =
      new MotionMagicConfigs()
          .withMotionMagicAcceleration(0)
          .withMotionMagicCruiseVelocity(0)
          .withMotionMagicJerk(0);

  public static final TalonFXConfiguration turretMotorConfig =
      new TalonFXConfiguration().withFeedback(feedbackConfig).withMotionMagic(motionMagicConfig);
}
