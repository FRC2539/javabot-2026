package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretSubsystem extends SubsystemBase {

  private final TurretIO io;
  private final TurretIO.TurretIOInputs inputs = new TurretIO.TurretIOInputs();

  public TurretSubsystem(TurretIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
  }

  public boolean atTarget() {
    return io.isAtSetpoint();
  }

  public Command setTargetHeading(Rotation2d desiredAngle) {
    return run(() -> io.setTargetHeading(desiredAngle));
  }

  public Command runVoltage(double volts) {
    return run(() -> io.setVoltage(volts));
  }
}
