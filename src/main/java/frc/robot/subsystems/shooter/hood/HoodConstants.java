package frc.robot.subsystems.shooter.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.math.util.Units;

public final class HoodConstants {

  // CAN
  public static final int kMotorId = 31;
  public static final String kCanBus = "rio";
  public static final double kGearRatio = 120.0;

  // Hard mechanical limits
  public static final double kMinAngleRad = Units.degreesToRadians(19.0);
  public static final double kMaxAngleRad = Units.degreesToRadians(51.0);

  public static final double angleDeadband = Units.degreesToRadians(1);
  public static final TalonFXConfiguration motorConfig = new TalonFXConfiguration();
}
