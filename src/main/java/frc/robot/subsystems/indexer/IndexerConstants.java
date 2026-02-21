package frc.robot.subsystems.indexer;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

public final class IndexerConstants {

  public static final int indexerMotorID = 16;
  public static final String canBus = "";

  public static final int transportMotorID = 18;
  public static final double currentLimit = 40.0;
  public static final double indexerMotorShootVoltage = 8.0;

  public static final double transportMotorStartVoltage = 1.0;

  public static final CurrentLimitsConfigs currentLimits =
      new CurrentLimitsConfigs().withSupplyCurrentLimit(currentLimit);
}
