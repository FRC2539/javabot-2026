package frc.robot.subsystems.shooter.flywheel;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class FlywheelSubsystem extends SubsystemBase {
  private FlywheelIO shooterIO;
  public FlywheelIOInputsAutoLogged shooterInputs = new FlywheelIOInputsAutoLogged();

  public FlywheelSubsystem(FlywheelIO io) {
    shooterIO = io;

    //setDefaultCommand(setShooterRPM(ShooterConstants.IdleRPM));
  }

  @Override
  public void periodic() {
    shooterIO.updateInputs(shooterInputs);
    Logger.processInputs("RealOutputs/Shooter", shooterInputs);
  }

  private void setTargetRPM(double targetRPM) {
    shooterIO.setControlVelocity(targetRPM / 60);
  }

  public Command setShooterRPM(double desiredRPM) {
    return run(() -> setTargetRPM(desiredRPM));
  }

  public Command setVoltage(double volts) {
    return Commands.run(() -> shooterIO.setVoltage(volts), this);
  }
}
