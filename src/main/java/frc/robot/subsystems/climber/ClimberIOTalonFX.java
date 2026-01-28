package frc.robot.subsystems.climber;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.ClimberConstants;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;


public class ClimberIOTalonFX implements ClimberIO {

  //#region Motors
  private final TalonFX leftMotor = new TalonFX(ClimberConstants.leftMotorId, ClimberConstants.canBus);
  private final TalonFX rightMotor = new TalonFX(ClimberConstants.rightMotorId, ClimberConstants.canBus);

  public ClimberIOTalonFX() {
    CurrentLimitsConfigs currentLimits = new CurrentLimitsConfigs();
    currentLimits.SupplyCurrentLimit = ClimberConstants.currentLimit;
    currentLimits.StatorCurrentLimit = ClimberConstants.currentLimit;
    currentLimits.StatorCurrentLimitEnable = true;
    currentLimits.StatorCurrentLimitEnable = true;

    TalonFXConfiguration config = new TalonFXConfiguration()
        .withCurrentLimits(currentLimits);

    leftMotor.getConfigurator().apply(config);
    rightMotor.getConfigurator().apply(config);

    leftMotor.setNeutralMode(NeutralModeValue.Brake);
    rightMotor.setNeutralMode(NeutralModeValue.Brake);
  }

  //#region IO
  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    inputs.leftPositionRotations = leftMotor.getPosition().refresh().getValueAsDouble();
    inputs.rightPositionRotations = rightMotor.getPosition().refresh().getValueAsDouble();
    inputs.leftVoltage = leftMotor.getMotorVoltage().refresh().getValueAsDouble();
    inputs.rightVoltage = rightMotor.getMotorVoltage().refresh().getValueAsDouble();
  }

  @Override
  public void setLeftPosition(double rotations) {
    leftMotor.setPosition(rotations);
  }

  @Override
  public void setRightPosition(double rotations) {
    rightMotor.setPosition(rotations);
  }

  @Override
  public void setLeftVoltage(double volts) {
    leftMotor.setVoltage(volts);
  }

  @Override
  public void setRightVoltage(double volts) {
    rightMotor.setVoltage(volts);
  }

  @Override
  public void setBrakeMode(boolean brake) {
    NeutralModeValue mode = brake ? NeutralModeValue.Brake : NeutralModeValue.Coast;
    leftMotor.setNeutralMode(mode);
    rightMotor.setNeutralMode(mode);
  }
}
