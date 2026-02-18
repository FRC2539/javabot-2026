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

  private double targetAngleRad = HoodConstants.kMinAngleRad;

  private final MotionMagicVoltage motorRequest = new MotionMagicVoltage(0);

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

    targetAngleRad = targetAngle.getRadians();

    motor.setControl(motorRequest.withPosition(targetAngle.getRadians()));
  }

  @Override
  public boolean isAtSetpoint() {

    double currentAngleRad = Units.rotationsToRadians(motor.getPosition().getValueAsDouble());

    double error = Math.abs(currentAngleRad - targetAngleRad);

    return error < HoodConstants.angleDeadband;
  }
}
