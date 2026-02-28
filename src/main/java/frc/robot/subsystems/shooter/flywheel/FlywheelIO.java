package frc.robot.subsystems.shooter.flywheel;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface FlywheelIO {
  public void updateInputs(FlywheelIOInputs inputs);

  public void setVoltage(double volts);

  @AutoLog
  public class FlywheelIOInputs {
    public double currentRPS = 0;
    public double setVoltage = 0;
    public double leftMotorTemperatureCelcius = 0;
    public double rightMotorTemperatureCelcius = 0;
  }

  @AutoLogOutput
  public boolean isAtSetpoint();

  @AutoLogOutput
  public void setControlVelocityRPS(double targetVelocity);
}
