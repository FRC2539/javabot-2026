package frc.robot.subsystems.indexer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IndexerIOTalonFX implements IndexerIO {

  private final TalonFX transportMotor =
    new TalonFX(IndexerConstants.indexerMotorID);

  private final TalonFX indexerMotor = new TalonFX(IndexerConstants.transportMotorID);

  public IndexerIOTalonFX() {
    TalonFXConfiguration transportConfig = new TalonFXConfiguration().withCurrentLimits(IndexerConstants.currentLimits);

    transportMotor.getConfigurator().apply(transportConfig);
    transportMotor.setNeutralMode(NeutralModeValue.Brake);

    indexerMotor.getConfigurator().apply(transportConfig);
  }

  @Override
  public void updateInputs(IndexerIOInputs inputs) {
    inputs.indexerMotorVoltage = indexerMotor.getMotorVoltage().getValueAsDouble();
    inputs.transportMotorVoltage = transportMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  @Override
  public void setVoltages(double indexerVoltage, double transportVoltage) {
    indexerMotor.setVoltage(indexerVoltage);
    transportMotor.setVoltage(transportVoltage);
  }
}
