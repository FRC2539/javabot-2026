package frc.robot.subsystems.hood;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
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
    Logger.processInputs("Hood", inputs);
  }

  public Command setHoodAngle(double angleDeg) {

    return run(() -> io.setTargetAngle(Rotation2d.fromDegrees(angleDeg)));
  }

  public boolean isAtSetpoint() {
    return io.isAtSetpoint();
  }
}
