package frc.robot.subsystems.shooter.turret;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public final class TurretConstants {

  public static final int turretMotorId = 17;
  public static final int turretEncoderID = 40;
  public static final String canBus = "rio";

  public static final double rotorToSensorRatio = 1.112288; // 59 , 335.63

  public static final Rotation2d minAngle = Rotation2d.fromRotations(-0.4206);
  public static final Rotation2d maxAngle = Rotation2d.fromRotations(0.43105);
  public static final double setpointToleranceRot = Units.degreesToRotations(2.5);

  //   public static final double maxVelRotPerSec = Units.radiansToRotations(8.0);

  //   public static final double maxAccelRotPerSec2 = Units.radiansToRotations(30.0);

  public static final Translation2d turretOffset = new Translation2d(-0.21, -0.171);

  public static final FeedbackConfigs feedbackConfig =
      new FeedbackConfigs()
          .withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)
          .withFeedbackRemoteSensorID(turretEncoderID)
          .withRotorToSensorRatio(rotorToSensorRatio);

  public static final Slot0Configs slot0configs = new Slot0Configs().withKP(60).withKS(4).withKD(4);

  public static final SoftwareLimitSwitchConfigs limitSwitchConfigs =
      new SoftwareLimitSwitchConfigs()
          .withForwardSoftLimitEnable(true)
          .withForwardSoftLimitThreshold(minAngle.getRotations())
          .withReverseSoftLimitEnable(true)
          .withReverseSoftLimitThreshold(maxAngle.getRotations());

  public static final MotorOutputConfigs outputConfigs =
      new MotorOutputConfigs().withInverted(InvertedValue.CounterClockwise_Positive);

  public static final MotionMagicConfigs motionMagicConfig =
      new MotionMagicConfigs()
          .withMotionMagicCruiseVelocity(500)
          .withMotionMagicAcceleration(2000)
          .withMotionMagicJerk(8000);

  public static final TalonFXConfiguration turretMotorConfig =
      new TalonFXConfiguration()
          .withFeedback(feedbackConfig)
          .withSlot0(slot0configs)
          .withMotorOutput(outputConfigs)
          .withMotionMagic(motionMagicConfig);
}
