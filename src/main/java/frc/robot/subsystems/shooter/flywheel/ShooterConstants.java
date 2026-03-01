package frc.robot.subsystems.shooter.flywheel;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public class ShooterConstants {
  public static final String shooterCanBus = "";

  public static final double goalDeadbandRPS = 2;
  public static final double GearRatioToWheel = 1.0714285714285714; // 15/14

  public static final int leftShooterMotorID = 12;
  public static final int rightShooterMotorID = 13;

  public static final Slot0Configs SlotConfigs =
      new Slot0Configs().withKP(.4).withKI(0).withKD(0).withKS(0).withKA(0).withKV(.1);

  public static final CurrentLimitsConfigs currentLimits =
      new CurrentLimitsConfigs().withSupplyCurrentLimitEnable(true).withSupplyCurrentLimit(80);
  // .withStatorCurrentLimit(100);

  //   public static final FeedbackConfigs feedbackConfig =
  //       new FeedbackConfigs().withSensorToMechanismRatio(GearRatioToWheel);

  public static TalonFXConfiguration leftMotorConfig =
      new TalonFXConfiguration().withSlot0(SlotConfigs).withCurrentLimits(currentLimits);

  //   public static TalonFXConfiguration rightMotorConfig =
  //       new TalonFXConfiguration()
  //           .withSlot0(SlotConfigs)
  //           .withCurrentLimits(currentLimits)
  //           .withMotionMagic(motionMagicConfig);
}
