package frc.robot.subsystems.shooter.turret;

public interface TurretIO {

  class TurretIOInputs {
    public boolean motorConnected = false;

    public double positionRad = 0.0;
    public double velocityRadPerSec = 0.0;

    public double appliedVolts = 0.0;
    public double supplyCurrentAmps = 0.0;
    public double tempCelsius = 0.0;
  }

  default void updateInputs(TurretIOInputs inputs) {}

  default void setPositionRad(double positionRad) {}

  default void setVoltage(double volts) {}

  default void stop() {}
}
