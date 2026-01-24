package frc.robot.subsystems.Kirkulator;

import org.littletonrobotics.junction.AutoLog;

public interface KirkulatorIO {

  public void updateInputs(KirkulatorIO kirkulatorIO);

  @AutoLog
  public class KirkulatorIOInputs {

    public double voltage = 0.0;
    public double passiveVoltage = 0.0;
    public double shootingVoltage = 0.0;

  }

  public void setVoltage(double voltage);
}