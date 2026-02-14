package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.math.util.Units;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import edu.wpi.first.math.geometry.Translation2d;

public final class HoodConstants {

  // CAN
  public static final int kMotorId = 31;
  public static final int hoodEncoderID = 23;
  public static final String kCanBus = "rio";
  public static final double kGearRatio = 120.0;

  // Hard mechanical limits
  public static final double kMinAngleRad = Units.degreesToRadians(19.0);
  public static final double kMaxAngleRad = Units.degreesToRadians(51.0);

  public static final double angleDeadband = Units.degreesToRadians(1);
  public static final TalonFXConfiguration motorConfig = new TalonFXConfiguration();

  public static final FeedbackConfigs feedbackConfig =
      new FeedbackConfigs()
          .withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)
          .withFeedbackRemoteSensorID(hoodEncoderID);

    public static final TalonFXConfiguration hoodMotorConfig =
      new TalonFXConfiguration().withFeedback(feedbackConfig);
}
