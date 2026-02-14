package frc.robot.subsystems.indexer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IndexerIOTalonFX implements IndexerIO {

  private final TalonFX transportMotor =
    new TalonFX(IndexerConstants.transportMotorId, IndexerConstants.canBus);

  private final TalonFX indexerMotor = new TalonFX(IndexerConstants.kMotorId, IndexerConstants.kCanBus);

  public IndexerIOTalonFX() {
    CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs();
    currentLimits.SupplyCurrentLimit = IndexerConstants.currentLimit;
    currentLimits.StatorCurrentLimit = IndexerConstants.currentLimit;
    currentLimits.SupplyCurrentLimitEnable = true;
    currentLimits.StatorCurrentLimitEnable = true;
    
    TalonFXConfiguration transportConfig = new TalonFXConfiguration().withCurrentLimits(currentLimits);

    transportMotor.getConfigurator().apply(transportConfig);
    transportMotor.setNeutralMode(NeutralModeValue.Brake);

    indexerMotor.getConfigurator().apply(new TalonFXConfiguration());
    indexerMotor.setVoltage(0.0);
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
