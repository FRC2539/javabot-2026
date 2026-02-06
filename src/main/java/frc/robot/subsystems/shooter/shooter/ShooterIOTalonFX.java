package frc.robot.subsystems.shooter.shooter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.constants.ShooterConstants;

public class ShooterIOTalonFX implements ShooterIO {

  private double targetRPS = 0;

  private TalonFX leftMotor =
      new TalonFX(ShooterConstants.leftShooterMotorID, ShooterConstants.shooterCanBus);
  private TalonFX rightMotor =
      new TalonFX(ShooterConstants.rightShooterMotorID, ShooterConstants.shooterCanBus);
  private MotionMagicVelocityVoltage controlRequest = new MotionMagicVelocityVoltage(targetRPS);
  private Follower motorFollowerRequest =
      new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed);

  public ShooterIOTalonFX() {
    leftMotor.setNeutralMode(NeutralModeValue.Coast);
    rightMotor.setNeutralMode(NeutralModeValue.Coast);

    leftMotor.getConfigurator().apply(ShooterConstants.leftMotorConfig);
    rightMotor.getConfigurator().apply(ShooterConstants.rightMotorConfig);

    rightMotor.setControl(motorFollowerRequest);
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    // convert wheel velocity from RPS -> RPM
    inputs.wheelVelocity = leftMotor.getVelocity().getValueAsDouble() * 60;
    inputs.leftMotorTemperatureCelcius = leftMotor.getDeviceTemp().getValueAsDouble();
    inputs.rightMotorTemperatureCelcius = rightMotor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setControlVelocity(double targetVelocity) {
    this.targetRPS = targetVelocity;
    leftMotor.setControl(controlRequest.withVelocity(targetVelocity));
    rightMotor.setControl(motorFollowerRequest);
  }

  @Override
  public boolean isAtSetpoint() {
    double currentSpeed = leftMotor.getVelocity().getValueAsDouble();

    double errorRPS = Math.abs(targetRPS - currentSpeed);

    // convert deadband to rotations per second
    if (errorRPS < ShooterConstants.goalDeadbandRPM / 60) {
      return true;
    } else {
      return false;
    }
  }
}
