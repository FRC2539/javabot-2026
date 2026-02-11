package frc.robot.subsystems.Indexer;

import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.AutoLogOutput;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {

  private static final LoggedTunableNumber kirkulatorShootVolts =
      new LoggedTunableNumber("Hopper/ShootVolts", 12.0);
  private static final LoggedTunableNumber kirkulatorReverseVolts =
      new LoggedTunableNumber("Hopper/ReverseVolts", -12.0);
  private static final LoggedTunableNumber kirkulatorAdjustVolts =
      new LoggedTunableNumber("Hopper/AdjustVolts", 0.0);

  private IndexerIO indexerIO;

  @AutoLogOutput private Goal goal = Goal.STOP;

  public IndexerSubsystem(IndexerIO io) {
    this.indexerIO = io;
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
    indexerIO.setVoltage(indexerVoltage);
  }

  public enum Goal {
    SHOOT,
    REVERSE,
    STOP,
    ADJUST
  }
}
