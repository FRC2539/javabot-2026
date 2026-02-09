package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ForwardPerspectiveValue;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.LimelightHelpers;
import org.littletonrobotics.junction.AutoLogOutput;

public class AlignToClimberMT2 extends CommandBase {

  private final CommandSwerveDrivetrain drive;

  private final SwerveRequest.ApplyFieldSpeeds applyFieldSpeeds =
      new SwerveRequest.ApplyFieldSpeeds()
          .withDriveRequestType(DriveRequestType.Velocity)
          .withSteerRequestType(SteerRequestType.MotionMagicExpo)
          .withForwardPerspective(ForwardPerspectiveValue.BlueAlliance);

  @AutoLogOutput public Pose2d targetPose;

  private final double xOffset;
  private final double yOffset;
  private final Rotation2d rotationOffset;

  public AlignToClimberMT2(
      CommandSwerveDrivetrain drivetrain,
      Pose2d climberPose,
      double xOffset,
      double yOffset,
      Rotation2d rotationOffset) {

    this.drive = drivetrain;
    this.targetPose = climberPose;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.rotationOffset = rotationOffset;

    // addRequirements(drivetrain);
  }

//   @Override
//   public void initialize() {

//   }

  @Override
  public void execute() {

    Pose2d currentPose = new Pose2d();

    boolean leftHasTarget = LimelightHelpers.getTV("limelight-left");
    boolean rightHasTarget = LimelightHelpers.getTV("limelight-right");

    if (leftHasTarget || rightHasTarget) {
      if (leftHasTarget && (!rightHasTarget
          || LimelightHelpers.getTA("limelight-left") >=
LimelightHelpers.getTA("limelight-right"))) {
        currentPose =
LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight-left").pose;
      } else {
        currentPose =
LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight-right").pose;
      }
    }

    // compute transform from target (climber + offsets) to current pose
    Transform2d offsetTransform =
        new Transform2d(
            currentPose.getTranslation().minus(targetPose.getTranslation()),
            currentPose.getRotation().minus(targetPose.getRotation()));

    double xVel = xController.calculate(offsetTransform.getX());
    double yVel = yController.calculate(offsetTransform.getY());
    double thetaVel = thetaController.calculate(offsetTransform.getRotation().getRadians());

    if (!leftHasTarget && !rightHasTarget) {
      xVel = 0;
      yVel = 0;
      thetaVel = 0;
    }

    Rotation2d targetRotation = targetPose.getRotation();
    ChassisSpeeds tagRelative = new ChassisSpeeds(xVel, yVel, thetaVel);
    ChassisSpeeds fieldRelative = ChassisSpeeds.fromRobotRelativeSpeeds(tagRelative,
targetRotation);

    drive.setControl(applyFieldSpeeds.withSpeeds(fieldRelative));
  }

  @Override
  public boolean isFinished() {
    boolean finished =
        xController.atSetpoint() && yController.atSetpoint() && thetaController.atSetpoint();

    return finished;
  }
}
