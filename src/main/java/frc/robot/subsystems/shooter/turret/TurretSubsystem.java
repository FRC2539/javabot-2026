package frc.robot.subsystems.shooter.turret;

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

}
