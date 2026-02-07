package frc.robot.subsystems.Indexer;

import frc.robot.util.LoggedTunableNumber;
import frc.robot.util.Subsystemutil;
import org.littletonrobotics.junction.AutoLogOutput;

public class IndexerSubsystem extends Subsystemutil {

  private static final LoggedTunableNumber kirkulatorShootVolts =
      new LoggedTunableNumber("Hopper/ShootVolts", 12.0);
  private static final LoggedTunableNumber kirkulatorReverseVolts =
      new LoggedTunableNumber("Hopper/ReverseVolts", -12.0);
  private static final LoggedTunableNumber kirkulatorAdjustVolts =
      new LoggedTunableNumber("Hopper/AdjustVolts", 0.0);

  private IndexerIO kirkulatorIO;

  @AutoLogOutput private Goal goal = Goal.STOP;

  public IndexerSubsystem(IndexerIO kirkulatorIO) {
    this.kirkulatorIO = kirkulatorIO;
  }

  public void periodic() {

    double indexerVoltage = 0.0;

    switch (goal) {
      case SHOOT -> {
        indexerVoltage = kirkulatorShootVolts.get();
      }
      case REVERSE -> {
        indexerVoltage = kirkulatorReverseVolts.get();
      }
      case STOP -> {
        indexerVoltage = 0.0;
      }
      case ADJUST -> {
        indexerVoltage = kirkulatorAdjustVolts.get();
      }
    }
    kirkulatorIO.setVoltage(indexerVoltage);
  }

  @Override
  public void periodicAfterScheduler() {}

  public enum Goal {
    SHOOT,
    REVERSE,
    STOP,
    ADJUST
  }
}
