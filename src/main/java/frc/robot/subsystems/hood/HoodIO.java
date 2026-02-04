package frc.robot.subsystems.hood;

import org.littletonrobotics.junction.AutoLog;

public interface HoodIO {

  @AutoLog
  class HoodIOInputs {
    public boolean motorConnected = false;

    public double positionRad = 0.0;
    public double velocityRadPerSec = 0.0;

    public double appliedVolts = 0.0;
    public double supplyCurrentAmps = 0.0;
    public double torqueCurrentAmps = 0.0;
    public double tempCelsius = 0.0;
  }

  default void updateInputs(HoodIOInputs inputs) {}

  default void setBrakeMode(boolean brake) {}

  default void setMotionMagic(double positionRad) {}

  default void stop() {}
}
