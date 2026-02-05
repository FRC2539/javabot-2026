package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {

  public void updateInputs(IndexerIOInputs inputs);

  @AutoLog
  public class IndexerIOInputs {
    public double indexerVoltage = 0.0;
    public double transportVoltage = 0.0;
  }

  public void setIndexerVoltage(double voltage);

  public void setTransportVoltage(double voltage);
}
