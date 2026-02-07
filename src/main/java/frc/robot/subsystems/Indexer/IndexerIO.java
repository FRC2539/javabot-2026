package frc.robot.subsystems.Indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {

  public void updateInputs(IndexerIOInputs inputs);

  @AutoLog
  public class IndexerIOInputs {

    public double voltage = 0.0;
  }

  public void setVoltage(double voltage);
}
