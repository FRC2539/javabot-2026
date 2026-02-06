package frc.robot.subsystems.Kirkulator;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.constants.KirkulatorConstants;

public class KirkulatorIOTalonFX implements KirkulatorIO {

  private TalonFX kirkulatorMotor = new TalonFX(KirkulatorConstants.kirkulatorMotorID);

  public KirkulatorIOTalonFX() {
    kirkulatorMotor.setVoltage(0);

    TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    kirkulatorMotor.getConfigurator().apply(talonConfig);
  }

  public void updateInputs(KirkulatorIOInputs inputs) {
    inputs.voltage = kirkulatorMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  public void setVoltage(double voltage) {
    kirkulatorMotor.setVoltage(voltage);
  }
}
