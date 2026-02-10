package frc.robot.constants;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.util.Units;

public final class AlignConstants {

  private AlignConstants() {}

  // PID 
  public static final double Kp = 1.0;
  public static final double Ki = 0.0;
  public static final double Kd = 0.0;

  public static final double leftOffset = -0.2;
  public static final double rightOffset = 0.2;
  public static final double centerOffset = -0.3;

  public static final double leftAlign = Units.inchesToMeters(-7);
  public static final double rightAlign = Units.inchesToMeters(7);

  // Tolerances
  public static final double aligningAngleTolerance = Units.degreesToRadians(3);
  public static final double aligningXTolerance = Units.inchesToMeters(2);
  public static final double aligningYTolerance = Units.inchesToMeters(1);

  // Field layout
  public static final AprilTagFieldLayout fieldLayout =
      AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark);
}
