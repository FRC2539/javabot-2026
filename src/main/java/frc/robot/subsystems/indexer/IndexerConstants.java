package frc.robot.subsystems.indexer;

import frc.robot.util.LoggedTunableNumber;

public final class IndexerConstants {

  public static final int kMotorId = 16;
  public static final String kCanBus = "";

  public static final LoggedTunableNumber shootVolts =
      new LoggedTunableNumber("Indexer/ShootVolts", 8.0);

  public static final LoggedTunableNumber reverseVolts =
      new LoggedTunableNumber("Indexer/ReverseVolts", -8.0);

  public static final LoggedTunableNumber stopVolts =
      new LoggedTunableNumber("Indexer/StopVolts", 0.0);

  private IndexerConstants() {}
}
