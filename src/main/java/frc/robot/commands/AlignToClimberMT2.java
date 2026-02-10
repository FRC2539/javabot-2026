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
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.AlignConstants;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.LimelightHelpers;
import org.littletonrobotics.junction.AutoLogOutput;

public class AlignToClimberMT2 extends Command {

  private final CommandSwerveDrivetrain drive;

  private final SwerveRequest.ApplyFieldSpeeds applyFieldSpeeds =
      new SwerveRequest.ApplyFieldSpeeds()
          .withDriveRequestType(DriveRequestType.Velocity)
          .withSteerRequestType(SteerRequestType.MotionMagicExpo)
          .withForwardPerspective(ForwardPerspectiveValue.BlueAlliance);

  private final PIDController xController =
      new PIDController(AlignConstants.Kp, AlignConstants.Ki, AlignConstants.Kd);

  private final PIDController yController =
      new PIDController(AlignConstants.Kp, AlignConstants.Ki, AlignConstants.Kd);

  private final ProfiledPIDController thetaController =
      new ProfiledPIDController(
          AlignConstants.Kp,
          AlignConstants.Ki,
          AlignConstants.Kd,
          new TrapezoidProfile.Constraints(Math.toRadians(360), Math.toRadians(180)));

  @AutoLogOutput public Pose2d targetPose;
  private final double xOffset;
  private final double yOffset;
  private final Rotation2d rotationOffset;

  public AlignToClimberMT2(
      CommandSwerveDrivetrain drivetrain,
      Pose2d tagPose,
      double xOffset,
      double yOffset,
      Rotation2d rotationOffset) {

    this.drive = drivetrain;
    this.targetPose = tagPose;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.rotationOffset = rotationOffset;

    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    xController.reset();
    yController.reset();
    thetaController.reset(0);
    xController.setSetpoint(xOffset);
    yController.setSetpoint(yOffset);
    thetaController.setGoal(rotationOffset.getRadians());

    xController.setTolerance(AlignConstants.aligningXTolerance);
    yController.setTolerance(AlignConstants.aligningYTolerance);
    thetaController.setTolerance(AlignConstants.aligningAngleTolerance);
  }

  @Override
  public void execute() {

    Pose2d currentPose = new Pose2d();

    boolean leftHasTarget = LimelightHelpers.getTV("limelight-left");
    boolean rightHasTarget = LimelightHelpers.getTV("limelight-right");

    if (leftHasTarget || rightHasTarget) {
      if (leftHasTarget
          && (!rightHasTarget
              || LimelightHelpers.getTA("limelight-left")
                  >= LimelightHelpers.getTA("limelight-right"))) {
        currentPose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight-left").pose;
      } else {
        currentPose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight-right").pose;
      }
    }

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

    Rotation2d tagRotation = targetPose.getRotation();
    ChassisSpeeds tagRelativeSpeeds = new ChassisSpeeds(xVel, yVel, thetaVel);

    ChassisSpeeds fieldRelativeSpeeds =
        ChassisSpeeds.fromRobotRelativeSpeeds(tagRelativeSpeeds, tagRotation);

    drive.setControl(applyFieldSpeeds.withSpeeds(fieldRelativeSpeeds));
  }

  @Override
  public boolean isFinished() {
    return xController.atSetpoint() && yController.atSetpoint() && thetaController.atSetpoint();
  }
}
