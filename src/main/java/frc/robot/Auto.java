package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.constants.AlignConstants;
import frc.robot.constants.GlobalConstants;
import java.util.Optional;
import java.util.Set;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

public class Auto {
  private final LoggedDashboardChooser<Command> autoChooser;
  private RobotConfig config; // PathPlanner robot configuration
  private final SwerveRequest.ApplyRobotSpeeds autoApplySpeeds =
      new SwerveRequest.ApplyRobotSpeeds();

  private RobotContainer robotContainer;

  public Auto(RobotContainer container) {
    this.robotContainer = container;
    setUpPathPlanner(container.drivetrain);
    setUpNamedCommands();
    autoChooser = new LoggedDashboardChooser<>("Auto Routine", AutoBuilder.buildAutoChooser());
  }

  public void setUpPathPlanner(frc.robot.subsystems.drive.CommandSwerveDrivetrain drivetrain) {
    config = GlobalConstants.getRobotConfigPathplanner();

    AutoBuilder.configure(
        drivetrain::getRobotPose,
        drivetrain::resetPose,
        drivetrain::getChassisSpeeds,
        (speeds, feedforwards) ->
            drivetrain.setControl(
                autoApplySpeeds
                    .withSpeeds(speeds)
                    .withWheelForceFeedforwardsX(feedforwards.robotRelativeForcesXNewtons())
                    .withWheelForceFeedforwardsY(feedforwards.robotRelativeForcesYNewtons())),
        new PPHolonomicDriveController( // PPHolonomicController is the built in path
            // following controller for holonomic drive trains
            new PIDConstants(15, 0.0, 0.0), // Translation PID constants
            new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
            ),
        config,
        () -> {
          // Boolean supplier that controls when the path will be mirrored for the red
          // alliance
          Optional<Alliance> alliance = DriverStation.getAlliance();
          if (alliance.isPresent()) {
            return alliance.get() == DriverStation.Alliance.Red;
          }
          return false;
        },
        drivetrain);
  }

  public void setUpNamedCommands() {

    NamedCommands.registerCommand(
      "No",
      Commands.none()
  );

  }

  public Command getAutoCommand() {
    return autoChooser.get();
  }
}