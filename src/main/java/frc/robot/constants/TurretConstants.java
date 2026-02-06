package frc.robot.constants;

import edu.wpi.first.math.util.Units;

public final class TurretConstants {

  // CAN
  public static final int TurretMotorId = 20;
  public static final String CanBus = "rio";


  public static final double GearRatio = 100.0;

  // Limits
  public static final double MinRadAngle = Units.degreesToRadians(-360);
  public static final double MaxRadAngle = Units.degreesToRadians(360);

  // Motion Magic
  public static final double MaxVelRadPerSec = 8.0;
  public static final double MaxAccelRadPerSec2 = 30.0;


  public static final double RadTolerance = Units.degreesToRadians(1.5);

  public static final double LoopPeriodSecs = 0.02;

  private TurretConstants() {}
}
