package frc.robot.constants;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

public class ClimberConstants {
  public static final int kMotorId = 30;
  public static final String kCanBus = "rio";

  public static final double kP = 80.0;
  public static final double kD = 0.0;

  public static final double kUpPosition = 50.0;
  public static final double kDownPosition = 5.0;

  public static final double kTolerance = 0.25;
}
