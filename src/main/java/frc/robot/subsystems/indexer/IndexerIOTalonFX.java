package frc.robot.subsystems.indexer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

public class IndexerIOTalonFX implements IndexerIO {

  private TalonFX indexerMotor = new TalonFX(IndexerConstants.indexerMotorID);
  private TalonFX transportMotor = new TalonFX(IndexerConstants.transportMotorID);

  public IndexerIOTalonFX() {

    TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    indexerMotor.getConfigurator().apply(talonConfig);
  }

  public void updateInputs(IndexerIOInputs inputs) {
    inputs.indexerVoltage = indexerMotor.getMotorVoltage().refresh().getValueAsDouble();
    inputs.transportVoltage = transportMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  public void setIndexerVoltage(double voltage) {
    indexerMotor.setVoltage(voltage);
  }

  public void setTransportVoltage(double voltage) {
    transportMotor.setVoltage(voltage);
  }
}
