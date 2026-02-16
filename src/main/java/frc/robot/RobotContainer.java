package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.climber.ClimberConstants;
import frc.robot.subsystems.climber.ClimberIOTalonFX;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.TunerConstants;
import frc.robot.subsystems.indexer.IndexerIOTalonFX;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.raspberry.PneumaticsIORevPH;
import frc.robot.subsystems.raspberry.PneumaticsSubsystem;
import frc.robot.subsystems.roller.RollerIOTalonFX;
import frc.robot.subsystems.roller.RollerSubsystem;
import frc.robot.subsystems.shooter.hood.HoodIOTalonFX;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;
import frc.robot.subsystems.shooter.turret.TurretIOTalonFX;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionSubsystem;

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

  public final ClimberSubsystem climber = new ClimberSubsystem(new ClimberIOTalonFX());

  public final RollerSubsystem roller = new RollerSubsystem(new RollerIOTalonFX());

  public final IndexerSubsystem indexer = new IndexerSubsystem(new IndexerIOTalonFX());

  public final PneumaticsSubsystem pneumatics = new PneumaticsSubsystem(new PneumaticsIORevPH());

  public final TurretSubsystem turret = new TurretSubsystem(new TurretIOTalonFX());

  public final HoodSubsystem hood = new HoodSubsystem(new HoodIOTalonFX());

  public final TargetingSubsystem targeting = new TargetingSubsystem(drivetrain);

  public final Auto auto;

  public final VisionSubsystem vision =
      new VisionSubsystem(
          drivetrain::filterAndAddMeasurements,
          new VisionIOLimelight("limelight-left", drivetrain::getRotation),
          new VisionIOLimelight("limelight-left", drivetrain::getRotation),
          new VisionIOLimelight("limelight-left", drivetrain::getRotation),
          new VisionIOLimelight("limelight-left", drivetrain::getRotation));

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

    turret.setDefaultCommand(turret.setTargetHeading(targeting.getIdealTurretAngle()));
  }

  private void configureBindings() {

    operatorController
        .getLeftTrigger()
        .whileTrue(climber.setVoltage(ClimberConstants.climberUpVoltage));
    operatorController
        .getRightTrigger()
        .whileTrue(climber.setVoltage(ClimberConstants.climberDownVoltage));

    rightDriveController.getLeftThumb().onTrue(pneumatics.toggleIntake());
    // operatorController.getDPadUp().onTrue(pneumatics.toggleRaspberry()); // v (its a secret)
    operatorController.getDPadLeft().onTrue(pneumatics.toggleRaspberry2());

    rightDriveController.getTrigger().whileTrue(roller.runPositiveVoltage(10.0));
    operatorController.getA().whileTrue(roller.runNegativeVoltage(10.0));
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
