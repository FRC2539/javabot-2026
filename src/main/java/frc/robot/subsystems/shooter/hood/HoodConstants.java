package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.CommutationConfigs;
import com.ctre.phoenix6.configs.ExternalFeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.signals.ExternalFeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import edu.wpi.first.math.geometry.Rotation2d;

public final class HoodConstants {

  // CAN
  public static final int kMotorId = 11;
  public static final int hoodEncoderID = 41;
  public static final String kCanBus = "rio";

  // Mechanism Limits
  public static final Rotation2d maxHoodAngle = Rotation2d.fromRotations(0.151);
  public static final Rotation2d minHoodAngle = Rotation2d.fromRotations(0.03125);

  public static final Rotation2d angleDeadband = Rotation2d.fromRotations(0.01);
  public static final TalonFXConfiguration motorConfig = new TalonFXConfiguration();

  public static final ExternalFeedbackConfigs feedbackConfig =
      new ExternalFeedbackConfigs()
          .withExternalFeedbackSensorSource(ExternalFeedbackSensorSourceValue.RemoteCANcoder)
          .withFeedbackRemoteSensorID(hoodEncoderID)
          .withSensorToMechanismRatio(3.142857074737549);

  public static final Slot0Configs slot0configs = new Slot0Configs().withKP(35).withKS(5);

  public static final MotorOutputConfigs outputConfigs =
      new MotorOutputConfigs().withInverted(InvertedValue.CounterClockwise_Positive);



  public static final TalonFXSConfiguration hoodMotorConfig =
      new TalonFXSConfiguration()
          .withExternalFeedback(feedbackConfig)
          .withSlot0(slot0configs)
          .withMotorOutput(outputConfigs)
          .withCommutation(
              new CommutationConfigs().withMotorArrangement(MotorArrangementValue.Minion_JST));
}
