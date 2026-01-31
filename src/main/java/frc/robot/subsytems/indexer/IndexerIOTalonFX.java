package frc.robot.subsytems.indexer;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.IndexerConstants;

public class IndexerIOTalonFX implements IndexerIO {

  TalonFX indexerMotor = new TalonFX(0);

  public IndexerIOTalonFX() {
    indexerMotor.setPosition(0);

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
