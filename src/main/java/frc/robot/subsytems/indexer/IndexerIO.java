package frc.robot.subsytems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {

  public void updateInputs(IndexerIOInputs inputs);

  @AutoLog
  public class IndexerIOInputs {
    double voltage = 0.0;
  }

  public void setVoltage(double voltage);
}
