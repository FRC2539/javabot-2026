package frc.robot.subsystems.roller;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class RollerIOTalonFXS implements RollerIO {
  // #region Motor
  private final TalonFXS rollerMotor =
      new TalonFXS(IntakeConstants.rollerMotorId, IntakeConstants.rollerMotorCanBus);

  public RollerIOTalonFXS() {

    TalonFXSConfiguration rollerConfig =
        new TalonFXSConfiguration().withCurrentLimits(IntakeConstants.rollerCurrentLimit);

    rollerMotor.getConfigurator().apply(rollerConfig);
    rollerMotor.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void updateInputs(RollerIOInputs inputs) {
    inputs.rollerVoltage = rollerMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  // #region Methods
  @Override
  public void setRollerVoltage(double voltage) {
    rollerMotor.setVoltage(voltage);
  }
}
