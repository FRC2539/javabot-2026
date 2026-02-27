package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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

  public Command goToAngleCommand(Rotation2d angle) {
    return Commands.runOnce(() -> this.setTargetAngle(angle), this)
        .andThen(Commands.run(() -> {}, this)); // .until(this::isAtSetpoint)
  }

  public void setTargetAngle(Rotation2d angle) {

    io.setTargetHeading(angle);
  }

  public boolean isAtSetpoint() {
    return io.isAtSetpoint();
  }

  public Command setVoltage(double volts) {
    return Commands.run(() -> io.setVoltage(volts), this);
  }
}
