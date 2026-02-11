package frc.robot.subsystems.climber;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

public class ClimberConstants {
  public static final int kMotorId = 30;
  public static final String kCanBus = "";

  public static final TalonFXConfiguration motorConfiguration = new TalonFXConfiguration();

  public static final double climberUpVoltage = -10;
  public static final double climberDownVoltage = 10;
}
