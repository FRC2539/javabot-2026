package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.LimelightHelpers.PoseEstimate;
import java.util.function.Consumer;
import org.littletonrobotics.junction.Logger;

public class VisionSubsystem extends SubsystemBase {

  private final VisionIO[] io;

  private final VisionIOInputsAutoLogged[] inputs;

  private Consumer<PoseEstimate> consumer;

  public VisionSubsystem(Consumer<PoseEstimate> consumer, VisionIO... visionIO) {

    this.io = visionIO;

    this.consumer = consumer;

    this.inputs = new VisionIOInputsAutoLogged[io.length];
    for (int i = 0; i < inputs.length; i++) {
      inputs[i] = new VisionIOInputsAutoLogged();
    }
  }

  @Override
  public void periodic() {
    double lowestAvgDistance = Double.POSITIVE_INFINITY;
    PoseEstimate bestPoseEstimate = null;
    for (int i = 0; i < io.length; i++) {
      io[i].updateInputs(inputs[i]);
      Logger.processInputs("Vision/Camera" + Integer.toString(i), inputs[i]);

      PoseEstimate currentPoseEstimate = io[i].getPoseEstimateMT2();

      if (LimelightHelpers.validPoseEstimate(currentPoseEstimate)) {
      //  if (currentPoseEstimate.avgTagDist < lowestAvgDistance) {
        lowestAvgDistance = currentPoseEstimate.avgTagDist;
        bestPoseEstimate = currentPoseEstimate;
          //System.out.println("Merged Pose!");
        //}
      }
      
      // if (LimelightHelpers.validPoseEstimate(currentPoseEstimate)) {
      //   consumer.accept(currentPoseEstimate);
      // }
    }

    if (bestPoseEstimate != null) {
        consumer.accept(bestPoseEstimate);
    }


    for (VisionIOInputsAutoLogged input : inputs) {
      Logger.processInputs("RealOutputs/Vision", input);
    }
  }
}
