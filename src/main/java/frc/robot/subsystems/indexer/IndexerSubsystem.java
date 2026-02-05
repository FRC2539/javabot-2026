package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {

  private IndexerIO indexerIO;

  public IndexerSubsystem(IndexerIO io) {
    this.indexerIO = io;
  }

  private void setVoltages(double indexerVoltage, double transportVoltage) {
    indexerIO.setIndexerVoltage(transportVoltage);
    indexerIO.setTransportVoltage(transportVoltage);
  }

  public Command FeedToShooter() {
    return run(
        () -> {
          setVoltages(IndexerConstants.indexerFeedVoltage, IndexerConstants.transportFeedVoltage);
        });
  }

  public Command stop() {
    return runOnce(() -> setVoltages(0, 0));
  }
}
