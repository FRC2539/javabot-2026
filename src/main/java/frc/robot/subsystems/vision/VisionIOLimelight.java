package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.vision.LimelightHelpers.PoseEstimate;
import java.util.function.Supplier;

public class VisionIOLimelight implements VisionIO {

  String cameraName = "";
  Supplier<Rotation2d> currentHeading;

  public VisionIOLimelight(String cameraName, Supplier<Rotation2d> currentHeading) {
    this.cameraName = cameraName;
    this.currentHeading = currentHeading;
    LimelightHelpers.Flush();
  }

  @Override
  public void updateInputs(VisionIOInputs inputs) {
    LimelightHelpers
        .Flush();

    inputs.targetX = LimelightHelpers.getTX(cameraName);
    inputs.targetY = LimelightHelpers.getTY(cameraName);
    inputs.hasTarget = LimelightHelpers.getTV(cameraName);
    inputs.targetA = LimelightHelpers.getTA(cameraName);

    // inputs.targetXNC = LimelightHelpers.getTXNC(cameraName);
    // inputs.targetYNC = LimelightHelpers.getTYNC(cameraName);

    updateHeading(currentHeading);
  }

  @Override
  public PoseEstimate getPoseEstimateMT2() {
    return LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cameraName);
  }

  @Override
  public void updateHeading(Supplier<Rotation2d> currentHeading) {
    LimelightHelpers.SetRobotOrientation(
        cameraName, currentHeading.get().getDegrees(), 0, 0, 0, 0, 0);
  }
}