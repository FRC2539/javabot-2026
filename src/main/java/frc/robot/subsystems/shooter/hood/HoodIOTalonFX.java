package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class HoodIOTalonFX implements HoodIO {

  private final CANcoder hoodEncoder = new CANcoder(HoodConstants.hoodEncoderID);
  private final TalonFX motor = new TalonFX(HoodConstants.kMotorId, HoodConstants.kCanBus);

  public double targetAngle = 0; // rotations

  public final MotionMagicVoltage motorRequest = new MotionMagicVoltage(targetAngle);

  public HoodIOTalonFX() {
    CANcoderConfiguration encoderConfig = new CANcoderConfiguration();
    hoodEncoder.getConfigurator().apply(encoderConfig);
    motor.getConfigurator().apply(HoodConstants.hoodMotorConfig);

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
}
