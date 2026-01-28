package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {

  void updateInputs(ClimberIOInputs inputs);

  void setLeftVoltage(double volts);
  void setRightVoltage(double volts);

  @AutoLog
  class ClimberIOInputs {
    public double leftPosition = 0.0;
    public double rightPosition = 0.0;

    public double leftVoltage = 0.0;
    public double rightVoltage = 0.0;
  }
}
