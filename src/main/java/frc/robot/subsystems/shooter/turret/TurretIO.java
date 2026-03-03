package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;

public interface TurretIO {

  public void setVoltage(double volts);

  @AutoLog
  class TurretIOInputs {
    public double positionDeg = 0.0;
    public double velocityDegPerSec = 0.0;
    public double voltage = 0.0;
    public double tempCelsius = 0.0;
    public boolean atSetpoint = false;
  }

  void updateInputs(TurretIOInputs inputs);

  void setTargetHeading(Rotation2d desiredAngle);

  boolean isAtSetpoint();
}
