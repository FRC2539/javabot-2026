package frc.robot.subsystems.Kirkulator;

import org.littletonrobotics.junction.AutoLog;

public interface KirkulatorIO {

  public void updateInputs(KirkulatorIOInputs inputs);

  @AutoLog
  public class KirkulatorIOInputs {

    public double voltage = 0.0;

  }

  public void setVoltage(double voltage);
}