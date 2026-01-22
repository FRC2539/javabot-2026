package frc.robot.subsystems.roller;

import org.littletonrobotics.junction.AutoLog;

public interface RollerIO {

  public void updateInputs(RollerIOInputs rollerIO);

  @AutoLog
  public class RollerIOInputs {
    double rollerVoltage = 0;
  }

  public void setRollerVoltage(double voltage);
}