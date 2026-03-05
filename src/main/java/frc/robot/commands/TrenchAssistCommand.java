package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.ApplyFieldSpeeds;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.shooter.targeting.TargetingConstants;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class TrenchAssistCommand extends Command {

  CommandSwerveDrivetrain drivetrain;

  PIDController lateralAssistController = new PIDController(5, 0, 0.1);

  SwerveRequest.ApplyFieldSpeeds controlRequest =
      new ApplyFieldSpeeds()
          .withDriveRequestType(DriveRequestType.Velocity)
          .withSteerRequestType(SteerRequestType.Position);

  Pose2d trenchPose;

  Supplier<Pose2d> robotPose;
  DoubleSupplier driverXVel;
  DoubleSupplier driverYVel;
  DoubleSupplier driverRotVel;

  public TrenchAssistCommand(DoubleSupplier x, DoubleSupplier y, DoubleSupplier rot, CommandSwerveDrivetrain dt) {
    drivetrain = dt;
    driverXVel = x;
    driverYVel = y;
    driverRotVel = rot;
  }

  @Override
  public void initialize() {
    trenchPose = getClosestTrenchPose();
    lateralAssistController.setSetpoint(trenchPose.getY());
  }

  @Override
  public void execute() {
    robotPose = () -> drivetrain.getRobotPose();
    double distanceToTrench = Math.abs(robotPose.get().getX() - trenchPose.getX());
    double assistWeight = MathUtil.clamp((3.5 - distanceToTrench) / 2, 0.0, 1.0);// first number is start distance, second is transition range

    double yAssist = lateralAssistController.calculate(robotPose.get().getY(), trenchPose.getY());
    yAssist = MathUtil.clamp(yAssist, -2.5, 2.5);

    double blendedY = MathUtil.interpolate(driverYVel.getAsDouble(), yAssist, assistWeight);

    drivetrain.setControl(
        controlRequest.withSpeeds(
            new ChassisSpeeds(driverXVel.getAsDouble(), blendedY, driverRotVel.getAsDouble())));
  }

  public Pose2d getClosestTrenchPose() {
    Pose2d rightTrench = new Pose2d(4.626, 0.644, Rotation2d.kZero);
    Pose2d leftTrench = new Pose2d(4.626, 7.425, Rotation2d.kZero);

    if (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red) {
      rightTrench = new Pose2d(TargetingConstants.fieldLengthMeters - rightTrench.getX(), rightTrench.getY(), rightTrench.getRotation());
      leftTrench = new Pose2d(TargetingConstants.fieldLengthMeters - leftTrench.getX(), leftTrench.getY(), leftTrench.getRotation());
    }

 
    return robotPose.get().nearest(List.of(rightTrench, leftTrench));
  }
}
