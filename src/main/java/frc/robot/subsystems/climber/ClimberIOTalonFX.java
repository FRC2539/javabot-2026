package frc.robot.subsystems.climber;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.constants.ClimberConstants;

public class ClimberIOTalonFX implements ClimberIO {

  private final TalonFX motor =
      new TalonFX(ClimberConstants.kMotorId, ClimberConstants.kCanBus);

  private final PositionVoltage positionRequest = new PositionVoltage(0.0);

  public ClimberIOTalonFX() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    motor.getConfigurator().apply(config);

    Slot0Configs slot0 = new Slot0Configs();
    slot0.kP = ClimberConstants.kP;
    slot0.kD = ClimberConstants.kD;
    motor.getConfigurator().apply(slot0);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    inputs.motorConnected = motor.isConnected();
    inputs.positionRot = motor.getPosition().getValueAsDouble();
    inputs.velocityRotPerSec = motor.getVelocity().getValueAsDouble();
    inputs.appliedVolts = motor.getMotorVoltage().getValueAsDouble();
    inputs.supplyCurrentAmps = motor.getSupplyCurrent().getValueAsDouble();
    inputs.tempCelsius = motor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setPosition(double positionRot) {
    positionRequest.Position = positionRot;
    motor.setControl(positionRequest);
  }

  @Override
  public void setVoltage(double volts) {
    motor.setVoltage(volts);
  }

  @Override
  public void stop() {
    motor.stopMotor();
  }

  @Override
  public void setBrakeMode(boolean brake) {
    motor.setNeutralMode(
        brake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
  }
}