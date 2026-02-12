package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {

  @AutoLog
  class IndexerIOInputs {
    public double voltage = 0.0;
  }

  default void updateInputs(IndexerIOInputs inputs) {}

  default void setVoltage(double voltage) {}

  default void stop() {}
}
