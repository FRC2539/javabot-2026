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

  private Rotation2d targetAngle = HoodConstants.minHoodAngle;

  private final MotionMagicVoltage motorRequest =
      new MotionMagicVoltage(targetAngle.getRotations());

  public HoodIOTalonFX() {
    CANcoderConfiguration encoderConfig = new CANcoderConfiguration();
    hoodEncoder.getConfigurator().apply(encoderConfig);

    motor.getConfigurator().apply(HoodConstants.hoodMotorConfig);
    motor.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void updateInputs(HoodIOInputs inputs) {
    inputs.positionDeg = Units.rotationsToDegrees(motor.getPosition().getValueAsDouble());
    inputs.voltage = motor.getMotorVoltage().getValueAsDouble();
  }

  @Override
  public void setTargetAngle(Rotation2d desiredTargetAngle) {
    targetAngle = desiredTargetAngle;

    motor.setControl(motorRequest.withPosition(targetAngle.getRotations()));
  }

  @Override
  public boolean isAtSetpoint() {
    double currentAngleRot = motor.getPosition().getValueAsDouble(); // rotations

    double error = Math.abs(currentAngleRot - targetAngle.getRotations());

    return error < HoodConstants.angleDeadband.getRotations();
  }
}
