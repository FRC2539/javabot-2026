package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.commands.ShooterCommands;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.TunerConstants;
import frc.robot.subsystems.indexer.IndexerIOTalonFX;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.raspberry.PneumaticsIORevPH;
import frc.robot.subsystems.raspberry.PneumaticsSubsystem;
import frc.robot.subsystems.roller.RollerIOTalonFXS;
import frc.robot.subsystems.roller.RollerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelIOTalonFX;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.turret.TurretIOTalonFX;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;

public class RobotContainer {

  private final double maxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);

  private final double maxAngularRate = RotationsPerSecond.of(1.5).in(RadiansPerSecond);

  private final ThrustmasterJoystick leftDriveController = new ThrustmasterJoystick(0);

  private final ThrustmasterJoystick rightDriveController = new ThrustmasterJoystick(1);

  private final LogitechController operatorController = new LogitechController(2);

  private final SwerveRequest.FieldCentric driveRequest =
      new SwerveRequest.FieldCentric()
          .withDeadband(maxSpeed * 0.05)
          .withRotationalDeadband(maxAngularRate * 0.1)
          .withDriveRequestType(DriveRequestType.Velocity);

  // subsystems

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  // public final ClimberSubsystem climber = new ClimberSubsystem(new ClimberIOTalonFX());

  public final RollerSubsystem roller = new RollerSubsystem(new RollerIOTalonFXS());

  public final IndexerSubsystem indexer = new IndexerSubsystem(new IndexerIOTalonFX());

  public final PneumaticsSubsystem pneumatics = new PneumaticsSubsystem(new PneumaticsIORevPH());

  public final TurretSubsystem turret = new TurretSubsystem(new TurretIOTalonFX());

  // public final HoodSubsystem hood = new HoodSubsystem(new HoodIOTalonFXS());

  public final FlywheelSubsystem flywheel = new FlywheelSubsystem(new FlywheelIOTalonFX());

  // public final TargetingSubsystem targeting = new TargetingSubsystem(drivetrain);

  public final Auto auto;

  // public final VisionSubsystem vision =
  //     new VisionSubsystem(
  //         drivetrain::filterAndAddMeasurements,
  //         new VisionIOLimelight("limelight-turretleft", drivetrain::getHeading),
  //         new VisionIOLimelight("limelight-turretcenter", drivetrain::getHeading),
  //         new VisionIOLimelight("limelight-turretright", drivetrain::getHeading),
  //         new VisionIOLimelight("limelight-left", drivetrain::getHeading));

  public RobotContainer() {

    configureBindings();

    auto = new Auto(this);

    drivetrain.setDefaultCommand(
        drivetrain.applyRequest(
            () -> {
              ChassisSpeeds speeds = getDriverChassisSpeeds();

              return driveRequest
                  .withVelocityX(speeds.vxMetersPerSecond)
                  .withVelocityY(speeds.vyMetersPerSecond)
                  .withRotationalRate(speeds.omegaRadiansPerSecond);
            }));

    // turret.setDefaultCommand(turret.goToAngleCommand(targeting.getIdealTurretAngle()));
  }

  private void configureBindings() {

    // operatorController
    //     .getLeftTrigger()
    //     .whileTrue(climber.setVoltage(ClimberConstants.climberUpVoltage));
    // operatorController
    //     .getRightTrigger()
    //     .whileTrue(climber.setVoltage(ClimberConstants.climberDownVoltage));

    rightDriveController.getLeftThumb().onTrue(pneumatics.dropIntakeRaspberry2Deployed());
    // operatorController.getDPadUp().onTrue(pneumatics.toggleRaspberry()); // v (its a secret)

    rightDriveController.getTrigger().whileTrue(roller.setVoltage(8.0));

    operatorController.getB().onTrue(pneumatics.toggleRaspberry2());

    operatorController.getX().whileTrue(roller.setVoltage(-8.0));

    operatorController.getY().whileTrue(indexer.indexToShooter());

    operatorController
        .getDPadDownLeft()
        .onTrue(pneumatics.setIntakePosition(PneumaticsSubsystem.PneumaticPosition.FORWARD));

    operatorController
        .getDPadDownRight()
        .onTrue(pneumatics.setIntakePosition(PneumaticsSubsystem.PneumaticPosition.REVERSE));

    operatorController
        .getDPadUp()
        .onTrue(pneumatics.setRaspberry2Position(PneumaticsSubsystem.PneumaticPosition.FORWARD));

    operatorController
        .getDPadDown()
        .onTrue(pneumatics.setRaspberry2Position(PneumaticsSubsystem.PneumaticPosition.REVERSE));

    operatorController.getLeftTrigger().whileTrue(ShooterCommands.shootWheels(flywheel, indexer));

    operatorController.getRightTrigger().whileTrue(flywheel.setVoltage(5.0));

    // operatorController
    //     .getA()
    //     .whileTrue(ShooterCommands.holdToShoot(flywheel, hood, indexer, targeting));

    operatorController.getLeftBumper().whileTrue(turret.setVoltage(1.4));

    operatorController.getRightBumper().whileTrue(turret.setVoltage(-1.4));
  }

  private ChassisSpeeds getDriverChassisSpeeds() {
    return new ChassisSpeeds(getXVelocity(), getYVelocity(), getThetaVelocity());
  }

  private double getXVelocity() {
    return DriveConstants.MAX_TRANSLATIONAL_SPEED.in(MetersPerSecond)
        * -Math.pow(leftDriveController.getYAxis().get(), 3);
  }

  private double getYVelocity() {
    return DriveConstants.MAX_TRANSLATIONAL_SPEED.in(MetersPerSecond)
        * -Math.pow(leftDriveController.getXAxis().get(), 3);
  }

  private double getThetaVelocity() {
    return DriveConstants.MAX_ROTATIONAL_SPEED.in(RadiansPerSecond)
        * -Math.pow(rightDriveController.getXAxis().get(), 3);
  }

  public Command getAutonomousCommand() {
    return auto.getAutoCommand();
  }
}
