package frc.robot.subsystems.transporter;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.TransportConstants;

public class TransportIOTalonFX implements TransportIO {

  // #region Motor
  private final TalonFX transportMotor =
      new TalonFX(TransportConstants.transportMotorId, TransportConstants.canBus);

  public TransportIOTalonFX() {
    // Setup current limits
    CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs();
    currentLimits.SupplyCurrentLimit = TransportConstants.currentLimit;
    currentLimits.StatorCurrentLimit = TransportConstants.currentLimit;
    currentLimits.SupplyCurrentLimitEnable = true;
    currentLimits.StatorCurrentLimitEnable = true;

    TalonFXConfiguration config = new TalonFXConfiguration().withCurrentLimits(currentLimits);

    transportMotor.getConfigurator().apply(config);
    transportMotor.setNeutralMode(NeutralModeValue.Brake);
  }

  // #endregion

  // #region IO
  @Override
  public void updateInputs(TransportIOInputs inputs) {
    inputs.transportVoltage = transportMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  @Override
  public void setTransportVoltage(double volts) {
    transportMotor.setVoltage(volts);
  }
  // #endregion
}
