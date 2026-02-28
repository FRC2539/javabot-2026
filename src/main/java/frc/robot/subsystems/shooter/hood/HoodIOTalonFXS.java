package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class HoodIOTalonFXS implements HoodIO {

  private final CANcoder hoodEncoder = new CANcoder(HoodConstants.hoodEncoderID);

  private final TalonFXS motor = new TalonFXS(HoodConstants.kMotorId, HoodConstants.kCanBus);

  private Rotation2d targetAngle = HoodConstants.minHoodAngle;

  private final PositionVoltage motorRequest =
      new PositionVoltage(targetAngle.getRotations());

  public HoodIOTalonFXS() {
    CANcoderConfiguration encoderConfig = new CANcoderConfiguration();
    encoderConfig.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 1;
    encoderConfig.MagnetSensor.MagnetOffset = 0.30444351562;
    hoodEncoder.getConfigurator().apply(encoderConfig);

    motor.getConfigurator().apply(HoodConstants.hoodMotorConfig);
    motor.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void updateInputs(HoodIOInputs inputs) {
    inputs.positionRad = Units.rotationsToRadians(motor.getPosition().getValueAsDouble());
    inputs.voltage = motor.getMotorVoltage().getValueAsDouble();
  }

  @Override
  public void setTargetAngle(Rotation2d desiredTargetAngle) {
    targetAngle = desiredTargetAngle;

    targetAngle = Rotation2d.fromDegrees(MathUtil.clamp(targetAngle.getDegrees(), HoodConstants.minHoodAngle.getDegrees(), HoodConstants.maxHoodAngle.getDegrees()));
    motor.setControl(motorRequest.withPosition(targetAngle.getRotations()));
  }

  @Override
  public boolean isAtSetpoint() {
    double currentAngleRot = motor.getPosition().getValueAsDouble(); // rotations

    double error = Math.abs(currentAngleRot - targetAngle.getRotations());

    return error < HoodConstants.angleDeadband.getRotations();
  }

  @Override
  public void setHoodVoltage(double voltage) {
    motor.setVoltage(voltage);
  }
}
