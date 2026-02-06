package frc.robot.subsystems.shooter.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShooterConstants;
import frc.robot.subsystems.shooter.ShooterIOInputsAutoLogged;

import org.littletonrobotics.junction.Logger;

public class ShooterSubsystem extends SubsystemBase {
  private ShooterIO shooterIO;
  public ShooterIOInputsAutoLogged shooterInputs = new ShooterIOInputsAutoLogged();

  public ShooterSubsystem(ShooterIO io) {
    shooterIO = io;

    setDefaultCommand(setShooterRPM(ShooterConstants.IdleRPM));
  }

  @Override
  public void periodic() {
    shooterIO.updateInputs(shooterInputs);
    Logger.processInputs("RealOutputs/ShooterSubsystem", shooterInputs);
  }

  private void setTargetRPM(double targetRPM) {
    shooterIO.setControlVelocity(targetRPM / 60);
  }

  public Command setShooterRPM(double desiredRPM) {
    return run(() -> setTargetRPM(desiredRPM));
  }
}
