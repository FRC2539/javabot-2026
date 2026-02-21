package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import edu.wpi.first.math.geometry.Rotation2d;

public final class HoodConstants {

  // CAN
  public static final int kMotorId = 31;
  public static final int hoodEncoderID = 23;
  public static final String kCanBus = "rio";
  public static final double kGearRatio = 120.0;

  // Mechanism Limits
  public static final Rotation2d minHoodAngle = Rotation2d.fromDegrees(45.0);
  public static final Rotation2d maxHoodAngle = Rotation2d.fromDegrees(85.0);

  public static final Rotation2d angleDeadband = Rotation2d.fromDegrees(1);
  public static final TalonFXConfiguration motorConfig = new TalonFXConfiguration();

  public static final FeedbackConfigs feedbackConfig =
      new FeedbackConfigs()
          .withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)
          .withFeedbackRemoteSensorID(hoodEncoderID);

  public static final TalonFXConfiguration hoodMotorConfig =
      new TalonFXConfiguration().withFeedback(feedbackConfig);
}
