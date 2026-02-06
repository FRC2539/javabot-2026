package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {

  @AutoLog
  class ClimberIOInputs {
    public double voltage = 0.0;
  }

  default void updateInputs(ClimberIOInputs inputs) {}

  default void setVoltage(double volts) {}

  default void stop() {}

  default void setBrakeMode(boolean brake) {}
}
