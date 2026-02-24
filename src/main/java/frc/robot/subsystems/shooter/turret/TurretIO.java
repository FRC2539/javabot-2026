package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.geometry.Rotation2d;

public interface TurretIO {

  public void setVoltage(double volts);

  class TurretIOInputs {
    public double positionDeg = 0.0;
    public double velocityDegPerSec = 0.0;
    public double voltage = 0.0;
    public double tempCelsius = 0.0;
  }

  void updateInputs(TurretIOInputs inputs);

  void setTargetHeading(Rotation2d desiredAngle);

  boolean isAtSetpoint();

}
