package frc.robot.subsystems.shooter.turret;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class TurretIOTalonFX implements TurretIO {

  private final CANcoder turretEncoder = new CANcoder(TurretConstants.turretEncoderID);
  private final TalonFX motor = new TalonFX(TurretConstants.turretMotorId, TurretConstants.canBus);

  private Rotation2d targetAngle = Rotation2d.kZero;
  private final MotionMagicVoltage controlRequest = new MotionMagicVoltage(targetAngle.getRotations());

  public TurretIOTalonFX() {
    CANcoderConfiguration encoderConfig = new CANcoderConfiguration();
    encoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
    encoderConfig.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;

    turretEncoder.getConfigurator().apply(encoderConfig);
    motor.getConfigurator().apply(TurretConstants.turretMotorConfig);

    motor.setNeutralMode(NeutralModeValue.Brake);
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {
    inputs.positionDeg = Units.rotationsToDegrees(motor.getPosition().getValueAsDouble());

    inputs.velocityDegPerSec = Units.rotationsToDegrees(motor.getVelocity().getValueAsDouble());
  }

  @Override
  public void setTargetHeading(Rotation2d desiredAngle) {
    targetAngle = desiredAngle;
    motor.setControl(controlRequest.withPosition(targetAngle.getRotations()));
  }

  @Override
  public boolean isAtSetpoint() {
    return Math.abs(motor.getPosition().getValueAsDouble() - targetAngle.getRotations())
        < TurretConstants.setpointTolerance.getRotations();
  }
}
