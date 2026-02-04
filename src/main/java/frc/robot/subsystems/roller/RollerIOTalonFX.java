package frc.robot.subsystems.roller;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.IntakeConstants;

public class RollerIOTalonFX implements RollerIO {
  // #region Motor
  private final TalonFX rollerMotor =
      new TalonFX(IntakeConstants.rollerMotorId, IntakeConstants.rollerMotorCanBus);

  public RollerIOTalonFX() {

    TalonFXConfiguration rollerConfig =
        new TalonFXConfiguration().withCurrentLimits(IntakeConstants.rollerCurrentLimit);

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
