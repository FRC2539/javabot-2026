package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.GlobalConstants;
import frc.robot.constants.GlobalConstants.ControllerConstants;
import frc.robot.constants.TunerConstants;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.climber.ClimberIOTalonFX;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.raspberry.PneumaticsIORevPH;
import frc.robot.subsystems.raspberry.PneumaticsSubsystem;

public class RobotContainer {

  private final double maxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);

  private final double maxAngularRate = RotationsPerSecond.of(1.5).in(RadiansPerSecond);

  private final ThrustmasterJoystick leftDriveController =
      new ThrustmasterJoystick(ControllerConstants.LEFT_DRIVE_CONTROLLER);

  private final ThrustmasterJoystick rightDriveController =
      new ThrustmasterJoystick(ControllerConstants.RIGHT_DRIVE_CONTROLLER);

  private final LogitechController operatorController =
      new LogitechController(ControllerConstants.OPERATOR_CONTROLLER);

  private final SwerveRequest.FieldCentric driveRequest =
      new SwerveRequest.FieldCentric()
          .withDeadband(maxSpeed * 0.05)
          .withRotationalDeadband(maxAngularRate * 0.1)
          .withDriveRequestType(DriveRequestType.Velocity);

  // subsystems

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  public final ClimberSubsystem climber = new ClimberSubsystem(new ClimberIOTalonFX());

  public final PneumaticsSubsystem pneumatics = new PneumaticsSubsystem(new PneumaticsIORevPH());

  public final Auto auto;

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
  }

  private void configureBindings() {

    operatorController.getLeftTrigger().whileTrue(climber.runPositiveVoltage(10.0));

    operatorController.getRightTrigger().whileTrue(climber.runNegativeVoltage(10.0));

    rightDriveController.getLeftThumb().onTrue(pneumatics.toggleIntake());

    operatorController
        .getDPadUp()
        .onTrue(pneumatics.setRaspberryPosition(PneumaticsSubsystem.raspberryPosition.EXPANDED));
    operatorController
        .getDPadDown()
        .onTrue(
            pneumatics.setRaspberryPosition(PneumaticsSubsystem.raspberryPosition.RETRACTED)); // v

    operatorController
        .getDPadDownLeft()
        .onTrue(pneumatics.setRaspberry2Position(PneumaticsSubsystem.raspberry2Position.EXPANDED));
    operatorController
        .getDPadDownRight()
        .onTrue(pneumatics.setRaspberry2Position(PneumaticsSubsystem.raspberry2Position.RETRACTED));
  }

  private ChassisSpeeds getDriverChassisSpeeds() {
    return new ChassisSpeeds(getXVelocity(), getYVelocity(), getThetaVelocity());
  }

  private double getXVelocity() {
    return GlobalConstants.MAX_TRANSLATIONAL_SPEED.in(MetersPerSecond)
        * -squareWithSign(deadband(leftDriveController.getYAxis().get(), 0.1));
  }

  private double getYVelocity() {
    return GlobalConstants.MAX_TRANSLATIONAL_SPEED.in(MetersPerSecond)
        * -squareWithSign(deadband(leftDriveController.getXAxis().get(), 0.1));
  }

  private double getThetaVelocity() {
    return GlobalConstants.MAX_ROTATIONAL_SPEED.in(RadiansPerSecond)
        * -squareWithSign(deadband(rightDriveController.getXAxis().get(), 0.1));
  }

  private static double deadband(double value, double deadband) {
    if (Math.abs(value) < deadband) {
      return 0.0;
    }
    return value;
  }

  private static double squareWithSign(double value) {
    return value * Math.abs(value);
  }

  public Command getAutonomousCommand() {
    return auto.getAutoCommand();
  }
}
