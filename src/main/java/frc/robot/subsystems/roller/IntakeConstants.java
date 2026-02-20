package frc.robot.subsystems.roller;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

public class IntakeConstants {

  public static final int rollerMotorId = 15;
  public static final String rollerMotorCanBus = "CANivore";
  public static final CurrentLimitsConfigs rollerCurrentLimit = new CurrentLimitsConfigs();

  public static final double intakeVoltage = 6;
}
