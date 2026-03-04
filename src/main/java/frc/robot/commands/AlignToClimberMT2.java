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
import edu.wpi.first.math.geometry.Translation2d;
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

    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    xController.setTolerance(AlignConstants.aligningXTolerance);
    yController.setTolerance(AlignConstants.aligningYTolerance);
    thetaController.setTolerance(AlignConstants.aligningAngleTolerance);
  }

  @Override
  public void execute() {

    Pose2d currentPose = null;

    String[] cameras = {
      "limelight-left", "limelight-right", "limelight-turret", "limelight-climber"
    };

    double bestTA = 0;

    for (String cam : cameras) {
      if (LimelightHelpers.getTV(cam)) {
        double ta = LimelightHelpers.getTA(cam);
        if (ta > bestTA) {
          bestTA = ta;
          currentPose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cam).pose;
        }
      }
    }

    if (currentPose == null) {
      drive.setControl(applyFieldSpeeds.withSpeeds(new ChassisSpeeds()));
      return;
    }

    Rotation2d desiredRotation =
        targetPose.getRotation().minus(Rotation2d.fromDegrees(90)).plus(rotationOffset);

    Translation2d offsetTranslation =
        new Translation2d(xOffset, yOffset).rotateBy(targetPose.getRotation());

    Pose2d desiredPose =
        new Pose2d(targetPose.getTranslation().plus(offsetTranslation), desiredRotation);

    Transform2d error = new Transform2d(currentPose, desiredPose);

    double xVel = xController.calculate(error.getX());
    double yVel = yController.calculate(error.getY());
    double thetaVel = thetaController.calculate(error.getRotation().getRadians());

    drive.setControl(applyFieldSpeeds.withSpeeds(new ChassisSpeeds(xVel, yVel, thetaVel)));
  }

  @Override
  public boolean isFinished() {
    return xController.atSetpoint() && yController.atSetpoint() && thetaController.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    drive.setControl(applyFieldSpeeds.withSpeeds(new ChassisSpeeds()));
  }

  public static class AlignToRightPoleMT2 extends Command {

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

    public AlignToRightPoleMT2(
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

      thetaController.enableContinuousInput(-Math.PI, Math.PI);

      xController.setTolerance(AlignConstants.aligningXTolerance);
      yController.setTolerance(AlignConstants.aligningYTolerance);
      thetaController.setTolerance(AlignConstants.aligningAngleTolerance);
    }

    @Override
    public void execute() {

      Pose2d currentPose = null;

      String[] cameras = {
        "limelight-left", "limelight-right", "limelight-turret", "limelight-climber"
      };

      double bestTA = 0;

      for (String cam : cameras) {
        if (LimelightHelpers.getTV(cam)) {
          double ta = LimelightHelpers.getTA(cam);
          if (ta > bestTA) {
            bestTA = ta;
            currentPose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(cam).pose;
          }
        }
      }

      if (currentPose == null) {
        drive.setControl(applyFieldSpeeds.withSpeeds(new ChassisSpeeds()));
        return;
      }

      Rotation2d desiredRotation =
          targetPose.getRotation().minus(Rotation2d.fromDegrees(90)).plus(rotationOffset);

      Translation2d offsetTranslation =
          new Translation2d(xOffset, yOffset).rotateBy(targetPose.getRotation());

      Pose2d desiredPose =
          new Pose2d(targetPose.getTranslation().plus(offsetTranslation), desiredRotation);

      Transform2d error = new Transform2d(currentPose, desiredPose);

      double xVel = xController.calculate(error.getX());
      double yVel = yController.calculate(error.getY());
      double thetaVel = thetaController.calculate(error.getRotation().getRadians());

      drive.setControl(applyFieldSpeeds.withSpeeds(new ChassisSpeeds(xVel, yVel, thetaVel)));
    }

    @Override
    public boolean isFinished() {
      return xController.atSetpoint() && yController.atSetpoint() && thetaController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
      drive.setControl(applyFieldSpeeds.withSpeeds(new ChassisSpeeds()));
    }
  }
}
