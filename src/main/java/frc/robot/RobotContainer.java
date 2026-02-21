package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.TunerConstants;
import frc.robot.subsystems.indexer.IndexerIOTalonFX;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.roller.RollerIOTalonFXS;
import frc.robot.subsystems.roller.RollerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelIOTalonFX;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodIOTalonFX;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.turret.TurretIOTalonFX;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;
import frc.robot.subsystems.transporter.TransportIOTalonFX;
import frc.robot.subsystems.transporter.TransportSubsystem;

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

  public final TransportSubsystem transporter = new TransportSubsystem(new TransportIOTalonFX());

  public final HoodSubsystem hood = new HoodSubsystem(new HoodIOTalonFX());

  public final FlywheelSubsystem shooter = new FlywheelSubsystem(new FlywheelIOTalonFX());

  public final TurretSubsystem turret = new TurretSubsystem(new TurretIOTalonFX());

  // public final PneumaticsSubsystem pneumatics = new PneumaticsSubsystem(new PneumaticsIORevPH());

  // public final Auto auto;

  // public final VisionSubsystem vision =
  //     new VisionSubsystem(
  //         drivetrain::filterAndAddMeasurements,
  //         new VisionIOLimelight("limelight-turretright", drivetrain::getRotation),
  //         new VisionIOLimelight("limelight-turretcenter", drivetrain::getRotation),
  //         new VisionIOLimelight("limelight-turretback", drivetrain::getRotation),
  //         new VisionIOLimelight("limelight-left", drivetrain::getRotation));

  public RobotContainer() {

    configureBindings();

    // auto = new Auto(this);

    drivetrain.setDefaultCommand(
        drivetrain.applyRequest(
            () -> {
              ChassisSpeeds speeds = getDriverChassisSpeeds();

              return driveRequest
                  .withVelocityX(speeds.vxMetersPerSecond)
                  .withVelocityY(speeds.vyMetersPerSecond)
                  .withRotationalRate(speeds.omegaRadiansPerSecond);
            }));
  }

  private void configureBindings() {

    // operatorController
    //     .getLeftTrigger()
    //     .whileTrue(climber.setVoltage(ClimberConstants.climberUpVoltage));
    // operatorController
    //     .getRightTrigger()
    //     .whileTrue(climber.setVoltage(ClimberConstants.climberDownVoltage));

    // rightDriveController.getLeftThumb().onTrue(pneumatics.toggleIntake());
    // // operatorController.getDPadUp().onTrue(pneumatics.toggleRaspberry()); // v (its a secret)
    // operatorController.getDPadLeft().onTrue(pneumatics.toggleRaspberry2());

    rightDriveController.getTrigger().whileTrue(roller.runPositiveVoltage(10.0));
    operatorController.getA().whileTrue(roller.runNegativeVoltage(10.0));

    // simple commands for the first deploy

    // operatorController
    //     .getLeftTrigger()
    //     .whileTrue(climber.setVoltage(ClimberConstants.climberUpTestVoltage));

    // operatorController
    //     .getRightTrigger()
    //     .whileTrue(climber.setVoltage(ClimberConstants.climberDownTestVoltage));

    // operatorController.getDPadUp().onTrue(pneumatics.toggleIntake());

    // // operatorController.getDPadDownRight().onTrue(pneumatics.toggleRaspberry());
    // operatorController.getDPadLeft().onTrue(pneumatics.toggleRaspberry2());

    operatorController.getLeftBumper().whileTrue(roller.runPositiveVoltage(3.0));
    operatorController.getRightBumper().whileTrue(roller.runNegativeVoltage(3.0));

    operatorController.getY().whileTrue(indexer.setVoltage(9));
    //operatorController.getA().whileTrue(indexer.setVoltage(-3.0));

    operatorController.getX().whileTrue(transporter.setTransportVoltageCommand(3.0));
    operatorController.getB().whileTrue(transporter.setTransportVoltageCommand(-3.0));

    operatorController.getStart().whileTrue(shooter.setVoltage(4.0));
    operatorController.getBack().whileTrue(turret.runVoltage(4.0));
    operatorController.getDPadDown().whileTrue(hood.setVoltage(4.0));
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
    // return auto.getAutoCommand();

    return Commands.none();
  }
}
