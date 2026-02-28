package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
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
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.raspberry.PneumaticsSubsystem.PneumaticPosition;
import frc.robot.subsystems.roller.IntakeConstants;
import java.util.Optional;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

public class Auto {
  private final LoggedDashboardChooser<Command> autoChooser;
  private RobotConfig config; // PathPlanner robot configuration
  private final SwerveRequest.ApplyRobotSpeeds autoApplySpeeds =
      new SwerveRequest.ApplyRobotSpeeds().withDriveRequestType(DriveRequestType.Velocity);

  private RobotContainer robotContainer;

  public Auto(RobotContainer container) {
    this.robotContainer = container;
    setUpPathPlanner();
    setUpNamedCommands();
    autoChooser = new LoggedDashboardChooser<>("Auto Routine", AutoBuilder.buildAutoChooser());
  }

  public void setUpPathPlanner() {
    CommandSwerveDrivetrain drivetrain = robotContainer.drivetrain;
    config = DriveConstants.getRobotConfigPathplanner();

    AutoBuilder.configure(
        drivetrain::getRobotPose,
        drivetrain::resetPose,
        drivetrain::getRobotSpeeds,
        (speeds, feedforwards) ->
            drivetrain.setControl(
                autoApplySpeeds
                    .withSpeeds(speeds)
                    .withWheelForceFeedforwardsX(feedforwards.robotRelativeForcesXNewtons())
                    .withWheelForceFeedforwardsY(feedforwards.robotRelativeForcesYNewtons())),
        new PPHolonomicDriveController( // PPHolonomicController is the built in path
            // following controller for holonomic drive trains
            new PIDConstants(10, 0.0, 0.0), // Translation PID constants
            new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
            ),
        config,
        () -> {
          // Boolean supplier that controls when the path will be mirrored for the red alliance
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
        "roller-intake", robotContainer.roller.setVoltage(IntakeConstants.intakeVoltage));

    NamedCommands.registerCommand(
        "roller-extake", robotContainer.roller.setVoltage(-IntakeConstants.intakeVoltage));

    NamedCommands.registerCommand("roller-stop", robotContainer.roller.setVoltage(0));

    NamedCommands.registerCommand(
        "intake-deploy", robotContainer.pneumatics.setIntakePosition(PneumaticPosition.FORWARD));

    NamedCommands.registerCommand(
        "intake-retract", robotContainer.pneumatics.setIntakePosition(PneumaticPosition.REVERSE));

    NamedCommands.registerCommand(
        "raspberry2-expand",
        robotContainer.pneumatics.setRaspberry2Position(PneumaticPosition.FORWARD));

    NamedCommands.registerCommand(
        "raspberry2-retract",
        robotContainer.pneumatics.setRaspberry2Position(PneumaticPosition.REVERSE));

    // NamedCommands.registerCommand(
    //     "climber-up",
    //     Commands.sequence(
    //         robotContainer
    //             .climber
    //             .setVoltage(frc.robot.subsystems.climber.ClimberConstants.climberUpVoltage)
    //             .withTimeout(2.0)));

    // NamedCommands.registerCommand(
    //     "climber-down",
    //     Commands.sequence(
    //         robotContainer
    //             .climber
    //             .setVoltage(frc.robot.subsystems.climber.ClimberConstants.climberDownVoltage)
    //             .withTimeout(2.0)));

    NamedCommands.registerCommand(
        "intake",
        Commands.sequence(
            robotContainer.pneumatics.setRaspberry2Position(PneumaticPosition.FORWARD),
            Commands.waitSeconds(0.1),
            robotContainer.pneumatics.setIntakePosition(PneumaticPosition.FORWARD),
            robotContainer.roller.setVoltage(IntakeConstants.intakeVoltage)));

    NamedCommands.registerCommand(
        "deployintakewithkicker",
        Commands.parallel(
            robotContainer.roller.setVoltage(IntakeConstants.dropvoltage),
            robotContainer.pneumatics.setIntakePosition(PneumaticPosition.FORWARD)));

    // NamedCommands.registerCommand(
    //     "hold-to-shoot",
    //     ShooterCommands.holdToShoot(
    //         robotContainer.flywheel,
    //         robotContainer.hood,
    //         robotContainer.indexer,
    //         robotContainer.targeting));
  }

  public Command getAutoCommand() {
    return autoChooser.get();
  }
}
