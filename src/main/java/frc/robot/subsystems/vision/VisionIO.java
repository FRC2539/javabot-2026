package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.vision.LimelightHelpers.PoseEstimate;
import java.util.function.Supplier;
import org.littletonrobotics.junction.AutoLog;

public interface VisionIO {
  @AutoLog
  public static class VisionIOInputs {
    double targetX = 0;
    double targetY = 0;
    double targetA = 0;
    boolean hasTarget = false;
  }

  public void updateInputs(VisionIOInputs inputs);

  public PoseEstimate getPoseEstimateMT2();

  public void updateHeading(Supplier<Rotation2d> currentHeading);
}
