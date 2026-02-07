package frc.robot.subsystems.Indexer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.constants.KirkulatorConstants;

public class IndexerIOTalonFX implements IndexerIO {

  private TalonFX kirkulatorMotor = new TalonFX(KirkulatorConstants.kirkulatorMotorID);

  public IndexerIOTalonFX() {
    kirkulatorMotor.setVoltage(0);

    TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    kirkulatorMotor.getConfigurator().apply(talonConfig);
  }

  public void updateInputs(IndexerIOInputs inputs) {
    inputs.voltage = kirkulatorMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  public void setVoltage(double voltage) {
    kirkulatorMotor.setVoltage(voltage);
  }
}
