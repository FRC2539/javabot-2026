package frc.robot.subsystems.indexer;

import org.littletonrobotics.junction.AutoLog;

public interface IndexerIO {

  @AutoLog
  class IndexerIOInputs {
    public double indexerMotorVoltage = 0.0;
    public double transportMotorVoltage = 0.0;
  }

  void updateInputs(IndexerIOInputs inputs);

  void setVoltages(double indexerVoltage, double transportVoltage);
}
