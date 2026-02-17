package frc.robot.subsystems.shooter.hood;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class HoodSubsystem extends SubsystemBase {

  private final HoodIO io;
  private final HoodIOInputsAutoLogged inputs = new HoodIOInputsAutoLogged();

  public HoodSubsystem(HoodIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Hood", inputs);
  }

  public Command setHoodAngle(Rotation2d desiredAngle) {
    return Commands.runOnce(() -> this.setTargetAngle(desiredAngle), this)
        .andThen(Commands.run(() -> {}, this).until(this::isAtSetpoint));
  }

  public boolean isAtSetpoint() {
    return io.isAtSetpoint();
  }

  public void setTargetAngle(Rotation2d angle) {
    io.setTargetAngle(angle);
  }
}
