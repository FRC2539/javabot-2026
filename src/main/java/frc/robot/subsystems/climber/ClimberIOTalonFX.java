package frc.robot.subsystems.climber;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.ClimberConstants;

public class ClimberIOTalonFX implements ClimberIO {
  private final TalonFX motor = new TalonFX(ClimberConstants.kMotorId, ClimberConstants.kCanBus);

  public ClimberIOTalonFX() {
    motor.getConfigurator().apply(ClimberConstants.motorConfiguration);
    setBrakeMode(true);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    inputs.voltage = motor.getMotorVoltage().getValueAsDouble();
  }

  @Override
  public void setVoltage(double volts) {
    motor.setVoltage(volts);
  }

  @Override
  public void stop() {
    motor.stopMotor();
  }

  @Override
  public void setBrakeMode(boolean brake) {
    motor.setNeutralMode(brake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
  }
}
