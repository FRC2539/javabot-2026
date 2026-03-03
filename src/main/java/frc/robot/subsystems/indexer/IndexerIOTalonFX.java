package frc.robot.subsystems.indexer;

import com.ctre.phoenix6.hardware.TalonFX;

public class IndexerIOTalonFX implements IndexerIO {

  private final TalonFX transportMotor = new TalonFX(IndexerConstants.indexerMotorID);

  private final TalonFX indexerMotor = new TalonFX(IndexerConstants.transportMotorID);

  public IndexerIOTalonFX() {}

  @Override
  public void updateInputs(IndexerIOInputs inputs) {
    inputs.indexerMotorVoltage = indexerMotor.getMotorVoltage().getValueAsDouble();
    inputs.transportMotorVoltage = transportMotor.getMotorVoltage().getValueAsDouble();
  }

  @Override
  public void setVoltages(double indexerVoltage, double transportVoltage) {
    indexerMotor.setVoltage(indexerVoltage);
    transportMotor.setVoltage(transportVoltage);
  }
}
