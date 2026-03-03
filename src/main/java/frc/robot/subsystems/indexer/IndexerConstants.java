package frc.robot.subsystems.indexer;

import frc.robot.util.LoggedTunableNumber;

public final class IndexerConstants {
  public static final int indexerMotorID = 16;
  public static final String canBus = "";

  public static final int transportMotorID = 18;
  public static final double currentLimit = 40.0;

  public static final double indexerShootVoltage = -9;
  public static final double transportShootVoltage = 12;

  public static final LoggedTunableNumber shootVolts =
      new LoggedTunableNumber("Indexer/ShootVolts", 8.0);

  public static final LoggedTunableNumber transportShootVolts =
      new LoggedTunableNumber("Indexer/TransportShootVolts", 12.0);
}
