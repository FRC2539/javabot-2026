package frc.robot.subsystems.Indexer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.constants.IndexerConstants;

public class IndexerIOTalonFX implements IndexerIO {

  private TalonFX indexerMotor = new TalonFX(IndexerConstants.indexerMotorID);

  public IndexerIOTalonFX() {
    indexerMotor.setVoltage(0);

    TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    indexerMotor.getConfigurator().apply(talonConfig);
  }

  public void updateInputs(IndexerIOInputs inputs) {
    inputs.voltage = indexerMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  public void setVoltage(double voltage) {
    indexerMotor.setVoltage(voltage);
  }
}
