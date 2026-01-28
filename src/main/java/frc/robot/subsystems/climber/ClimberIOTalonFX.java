package frc.robot.subsystems.climber;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.ClimberConstants;

public class ClimberIOTalonFX implements ClimberIO {

  private final TalonFX left =
      new TalonFX(ClimberConstants.leftMotorId, ClimberConstants.canBus);

  private final TalonFX right =
      new TalonFX(ClimberConstants.rightMotorId, ClimberConstants.canBus);

  public ClimberIOTalonFX() {
    TalonFXConfiguration cfg = new TalonFXConfiguration();

    cfg.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    cfg.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    cfg.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    cfg.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
        ClimberConstants.upperLimit;
    cfg.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
        ClimberConstants.lowerLimit;

    left.getConfigurator().apply(cfg);
    right.getConfigurator().apply(cfg);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    inputs.leftPosition = left.getPosition().refresh().getValueAsDouble();
    inputs.rightPosition = right.getPosition().refresh().getValueAsDouble();

    inputs.leftVoltage = left.getMotorVoltage().refresh().getValueAsDouble();
    inputs.rightVoltage = right.getMotorVoltage().refresh().getValueAsDouble();
  }

  @Override
  public void setLeftVoltage(double volts) {
    left.setVoltage(volts);
  }

  @Override
  public void setRightVoltage(double volts) {
    right.setVoltage(volts);
  }
}
