package frc.robot.subsystems.roller;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

public class IntakeConstants {

  // #region Motors

  public static final int rollerMotorId = 15;
  public static final String rollerMotorCanBus = "rio"; // TODO: Correct CANbus
  public static final CurrentLimitsConfigs rollerCurrentLimit = new CurrentLimitsConfigs();

  // #region Constants

  public static final double intakeVoltage = 10; // TODO: Correct #
}
