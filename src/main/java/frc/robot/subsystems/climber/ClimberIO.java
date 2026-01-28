package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {

  void updateInputs(ClimberIOInputs inputs);

  @AutoLog
  class ClimberIOInputs {
    public double leftPositionRotations = 0.0;
    public double rightPositionRotations = 0.0;
    public double leftVoltage = 0.0;
    public double rightVoltage = 0.0;
  }

  void setLeftPosition(double rotations);
  void setRightPosition(double rotations);

  void setLeftVoltage(double volts);
  void setRightVoltage(double volts);

  void setBrakeMode(boolean brake);
}
