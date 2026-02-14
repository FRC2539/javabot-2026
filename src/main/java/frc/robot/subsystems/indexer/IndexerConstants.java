package frc.robot.subsystems.indexer;

import frc.robot.util.LoggedTunableNumber;

public final class IndexerConstants {

  public static final int kMotorId = 40;
  public static final String kCanBus = "";
  // #region Motor (Transport)
  public static final int transportMotorId = 20; // replace with motor CAN ID
  public static final String canBus = "CANivore"; // replace if needed
  public static final double currentLimit = 40.0;
  public static final double indexerMotorShootVoltage = 8.0;
  public static final double indexerMotorReverseVoltage = -indexerMotorShootVoltage;
  public static final double transportMotorStartVoltage = 1.0;
  public static final double transportMotorReverseVoltage = -transportMotorStartVoltage;
  // #endregion (Transport)
  
  private IndexerConstants() {}
}
