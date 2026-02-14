package frc.robot.subsystems.shooter.flywheel;

import org.littletonrobotics.junction.AutoLog;

public interface FlywheelIO {
  public void updateInputs(FlywheelIOInputs inputs);

  @AutoLog
  public class FlywheelIOInputs {
    public double wheelVelocity = 0; // RPM
    public double leftMotorTemperatureCelcius = 0;
    public double rightMotorTemperatureCelcius = 0;
    public double wheelVoltage = 0;
  }

  public boolean isAtSetpoint();

  public void setVoltage(double volts);

  public void setControlVelocity(double targetVelocity);
}
