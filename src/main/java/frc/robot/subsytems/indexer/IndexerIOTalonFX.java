package frc.robot.subsytems.indexer;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IndexerIOTalonFX implements IndexerIO {

  TalonFX indexerMotor = new TalonFX(IndexerConstants.indexerMotorID);

  public IndexerIOTalonFX() {
    indexerMotor.setVoltage(0);

    indexerMotor.getConfigurator().apply(IndexerConstants.indexerMotorConfig);
    indexerMotor.setNeutralMode(NeutralModeValue.Coast);
  }

  public void updateInputs(IndexerIOInputs inputs) {
    inputs.voltage = indexerMotor.getMotorVoltage().getValueAsDouble();
  }

  public void setVoltage(double voltage) {
    indexerMotor.setVoltage(voltage);
  }
}
