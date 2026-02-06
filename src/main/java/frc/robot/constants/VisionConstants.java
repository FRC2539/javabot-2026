package frc.robot.constants;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;

public class VisionConstants {
  // AprilTag layout
  public static AprilTagFieldLayout aprilTagLayout =
      AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

  // Camera names, must match names configured on coprocessor
  public static String camera0Name = "limelight-left";
  public static String camera1Name = "limelight-right";
  public static String camera2Name = "limelight-backleft";
  public static String camera3Name = "limelight-backright";

  // Basic filtering thresholds
  public static double maxZError = 2;
}
