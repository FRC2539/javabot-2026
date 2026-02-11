package frc.robot.subsystems.Indexer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

public class IndexerIOTalonFX implements IndexerIO {

  private final TalonFX motor =
      new TalonFX(IndexerConstants.kMotorId, IndexerConstants.kCanBus); 

  public IndexerIOTalonFX() {
    motor.getConfigurator().apply(new TalonFXConfiguration());
    motor.setVoltage(0.0);
  }

  @Override
  public void updateInputs(IndexerIOInputs inputs) {
    inputs.voltage = motor.getMotorVoltage().getValueAsDouble();
  }

  @Override
  public void setVoltage(double voltage) {
    motor.setVoltage(voltage);
  }

  @Override
  public void stop() {
    motor.stopMotor();
  }
}
