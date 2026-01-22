package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public class ShooterConstants {
    public static final String shooterCanBus = "";

    public static final double goalDeadbandRPS = 15; 
    public static final double IdleRPS = 1.5;

    public static final int leftShooterMotorID = 100;
    public static final int rightShooterMotorID = 100;

    public static final Slot0Configs SlotConfigs = new Slot0Configs()
        .withKP(0)
        .withKI(0)
        .withKD(0)
        .withKS(0)
        .withKA(0)
        .withKV(0);

    public static final CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs()
        .withSupplyCurrentLimit(100)
        .withStatorCurrentLimit(100);

    public static final MotionMagicConfigs motionMagicConfig = new MotionMagicConfigs()
        .withMotionMagicAcceleration(0)
        .withMotionMagicCruiseVelocity(0)
        .withMotionMagicJerk(0);

    public static TalonFXConfiguration leftMotorConfig = new TalonFXConfiguration()
        .withSlot0(SlotConfigs)
        .withCurrentLimits(currentLimits)
        .withMotionMagic(motionMagicConfig);

    public static TalonFXConfiguration rightMotorConfig = new TalonFXConfiguration()
        .withSlot0(SlotConfigs)
        .withCurrentLimits(currentLimits)
        .withMotionMagic(motionMagicConfig);
    
}
