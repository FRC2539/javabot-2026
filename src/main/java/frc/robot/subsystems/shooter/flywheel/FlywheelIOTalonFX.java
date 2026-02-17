package frc.robot.subsystems.shooter.flywheel;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class FlywheelIOTalonFX implements FlywheelIO {

  private double targetRPM = 0;

  private TalonFX leftMotor =
      new TalonFX(ShooterConstants.leftShooterMotorID, ShooterConstants.shooterCanBus);
  private TalonFX rightMotor =
      new TalonFX(ShooterConstants.rightShooterMotorID, ShooterConstants.shooterCanBus);
  private MotionMagicVelocityVoltage controlRequest =
      new MotionMagicVelocityVoltage(targetRPM / 60);
  private Follower motorFollowerRequest =
      new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed);

  public FlywheelIOTalonFX() {
    leftMotor.setNeutralMode(NeutralModeValue.Coast);
    rightMotor.setNeutralMode(NeutralModeValue.Coast);

    leftMotor.getConfigurator().apply(ShooterConstants.leftMotorConfig);
    rightMotor.getConfigurator().apply(ShooterConstants.rightMotorConfig);

    rightMotor.setControl(motorFollowerRequest);
  }

  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
    // convert wheel velocity from RPS -> RPM
    inputs.wheelVelocity = leftMotor.getVelocity().getValueAsDouble() * 60;
    inputs.leftMotorTemperatureCelcius = leftMotor.getDeviceTemp().getValueAsDouble();
    inputs.rightMotorTemperatureCelcius = rightMotor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setControlVelocity(double targetVelocity) {
    this.targetRPM = targetVelocity;
    leftMotor.setControl(controlRequest.withVelocity(targetVelocity));
    rightMotor.setControl(motorFollowerRequest);
  }

  @Override
  public boolean isAtSetpoint() {
    return Math.abs(leftMotor.getVelocity().getValueAsDouble() - targetRPM)
        < ShooterConstants.goalDeadbandRPM;
  }
}
