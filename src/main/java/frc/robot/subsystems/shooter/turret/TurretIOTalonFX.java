package frc.robot.subsystems.shooter.turret;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.constants.TurretConstants;

public class TurretIOTalonFX implements TurretIO {

  private final CANcoder turretEncoder = new CANcoder(TurretConstants.turretEncoderID);
  private final TalonFX motor = new TalonFX(TurretConstants.turretMotorId, TurretConstants.canBus);

  private final MotionMagicVoltage controlRequest = new MotionMagicVoltage(0.0);

  public TurretIOTalonFX() {
    CANcoderConfiguration x = new CANcoderConfiguration();
    x.MagnetSensor.AbsoluteSensorDiscontinuityPoint = .5;
    
    motor.getConfigurator().apply(TurretConstants.turretMotorConfig);

    motor.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {
    inputs.positionDeg =
        Units.rotationsToDegrees(motor.getPosition().getValueAsDouble());

    inputs.velocityDegPerSec =
        Units.rotationsToDegrees(motor.getVelocity().getValueAsDouble());

    inputs.voltage = motor.getMotorVoltage().getValueAsDouble();
    inputs.tempCelsius = motor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setTargetHeading(Rotation2d desiredAngle) {
    double positionRot = desiredAngle.getRotations();
    motor.setControl(controlRequest.withPosition(positionRot));
  }

  @Override
  public boolean isAtSetpoint() {
    double motorError = motor.getClosedLoopError().getValueAsDouble();
    return Math.abs(motorError) < TurretConstants.setpointToleranceRot;
  }
}
