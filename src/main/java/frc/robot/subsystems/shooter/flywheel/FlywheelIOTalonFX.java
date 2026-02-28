package frc.robot.subsystems.shooter.flywheel;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class FlywheelIOTalonFX implements FlywheelIO {

  private double targetRPS = 0;

  private TalonFX leftMotor =
      new TalonFX(ShooterConstants.leftShooterMotorID, ShooterConstants.shooterCanBus);
  private TalonFX rightMotor =
      new TalonFX(ShooterConstants.rightShooterMotorID, ShooterConstants.shooterCanBus);
  private VelocityVoltage controlRequest =
      new VelocityVoltage(targetRPS);
  private Follower motorFollowerRequest =
      new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed);

  public FlywheelIOTalonFX() {
    leftMotor.setNeutralMode(NeutralModeValue.Coast);
    rightMotor.setNeutralMode(NeutralModeValue.Coast);

    leftMotor.getConfigurator().apply(ShooterConstants.leftMotorConfig);
  
    rightMotor.setControl(motorFollowerRequest);
  }

  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
    inputs.wheelVelocity = leftMotor.getRotorVelocity().getValueAsDouble();
    inputs.leftMotorTemperatureCelcius = leftMotor.getDeviceTemp().getValueAsDouble();
    inputs.rightMotorTemperatureCelcius = rightMotor.getDeviceTemp().getValueAsDouble();
    inputs.setVoltage = leftMotor.getMotorVoltage().getValueAsDouble();
  }

  @Override
  public void setControlVelocityRPS(double targetVelocityRPS) {
    this.targetRPS = targetVelocityRPS;
    leftMotor.setControl(controlRequest.withVelocity(targetVelocityRPS));
    rightMotor.setControl(motorFollowerRequest);
  }

  @Override
  public boolean isAtSetpoint() {
    return Math.abs(leftMotor.getVelocity().getValueAsDouble() - targetRPS)
        < ShooterConstants.goalDeadbandRPS;
  }

  public void setVoltage(double voltage) {
    leftMotor.setVoltage(voltage);
  }
}
