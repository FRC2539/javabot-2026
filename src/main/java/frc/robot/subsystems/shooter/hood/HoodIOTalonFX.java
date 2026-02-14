package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class HoodIOTalonFX implements HoodIO {

  private final TalonFX motor = new TalonFX(HoodConstants.kMotorId, HoodConstants.kCanBus);

  public double targetAngle = 0; // radians

  private final MotionMagicVoltage motorRequest = new MotionMagicVoltage(targetAngle);

  public HoodIOTalonFX() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    motor.getConfigurator().apply(config);

    motor.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void updateInputs(HoodIOInputs inputs) {
    inputs.positionRad = Units.rotationsToRadians(motor.getPosition().getValueAsDouble());
    inputs.velocityRadPerSec = Units.rotationsToRadians(motor.getVelocity().getValueAsDouble());

    inputs.voltage = motor.getMotorVoltage().getValueAsDouble();
    inputs.tempCelsius = motor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setTargetAngle(Rotation2d targetAngle) {
    motor.setControl(motorRequest.withPosition(targetAngle.getRadians()));
  }

  @Override
  public boolean isAtSetpoint() {
    double currentAngle = Units.rotationsToRadians(motor.getPosition().getValueAsDouble());

    double error = Math.abs(currentAngle - targetAngle);

    return error < HoodConstants.angleDeadband;
  }

  @Override
  public void setHoodVoltage(double voltage) {
    motor.setVoltage(voltage);
  }
}
