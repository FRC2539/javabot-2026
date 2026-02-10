package frc.robot.subsystems.input;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.GlobalConstants;
import frc.robot.constants.GlobalConstants.ControllerConstants;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.climber.ClimberSubsystem;
import java.util.function.Supplier;

public class ControlSubsystem extends SubsystemBase {

  private final ThrustmasterJoystick leftDriveController =
      new ThrustmasterJoystick(ControllerConstants.LEFT_DRIVE_CONTROLLER);

  private final ThrustmasterJoystick rightDriveController =
      new ThrustmasterJoystick(ControllerConstants.RIGHT_DRIVE_CONTROLLER);

  private final LogitechController operatorController =
      new LogitechController(ControllerConstants.OPERATOR_CONTROLLER);

  public ControlSubsystem(ClimberSubsystem climberSubsystem) {
    operatorController.getLeftTrigger().whileTrue(climberSubsystem.runPositiveVoltage(10));

    operatorController.getRightTrigger().whileTrue(climberSubsystem.runNegativeVoltage(10));
  }

  public Supplier<ChassisSpeeds> getDriverChassisSpeeds() {
    return () -> new ChassisSpeeds(getXVelocity(), getYVelocity(), getThetaVelocity());
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
}
