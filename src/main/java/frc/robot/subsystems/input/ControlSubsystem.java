package frc.robot.subsystems.input;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.DriveConstants.ControllerConstants;

import java.util.function.Supplier;

public class ControlSubsystem extends SubsystemBase {

  private final ThrustmasterJoystick leftDriveController =
      new ThrustmasterJoystick(ControllerConstants.LEFT_DRIVE_CONTROLLER);

  private final ThrustmasterJoystick rightDriveController =
      new ThrustmasterJoystick(ControllerConstants.RIGHT_DRIVE_CONTROLLER);

  public ControlSubsystem() {
    // No bindings yet
  }

  public Supplier<ChassisSpeeds> getDriverChassisSpeeds() {
    return () -> new ChassisSpeeds(getXVelocity(), getYVelocity(), getThetaVelocity());
  }

  private double getXVelocity() {
    return DriveConstants.MAX_TRANSLATIONAL_SPEED.in(MetersPerSecond)
        * -squareWithSign(deadband(leftDriveController.getYAxis().get(), 0.1));
  }

  private double getYVelocity() {
    return DriveConstants.MAX_TRANSLATIONAL_SPEED.in(MetersPerSecond)
        * -squareWithSign(deadband(leftDriveController.getXAxis().get(), 0.1));
  }

  private double getThetaVelocity() {
    return DriveConstants.MAX_ROTATIONAL_SPEED.in(RadiansPerSecond)
        * -squareWithSign(deadband(rightDriveController.getXAxis().get(), 0.1));
  }

  // helpers

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
