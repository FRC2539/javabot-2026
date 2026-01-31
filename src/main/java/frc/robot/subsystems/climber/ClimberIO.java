package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {

  @AutoLog
  class ClimberIOInputs {
    public boolean motorConnected = false;

    public double positionRot = 0.0;
    public double velocityRotPerSec = 0.0;

    public double appliedVolts = 0.0;
    public double supplyCurrentAmps = 0.0;
    public double tempCelsius = 0.0;
  }

  default void updateInputs(ClimberIOInputs inputs) {}

  default void setPosition(double positionRot) {}

  default void setVoltage(double volts) {}

  default void stop() {}

  default void setBrakeMode(boolean brake) {}
}
