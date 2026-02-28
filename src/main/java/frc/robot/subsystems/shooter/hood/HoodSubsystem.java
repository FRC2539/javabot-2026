package frc.robot.subsystems.shooter.hood;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class HoodSubsystem extends SubsystemBase {

  private final HoodIO io;
  private final HoodIOInputsAutoLogged inputs = new HoodIOInputsAutoLogged();

  public HoodSubsystem(HoodIO io) {
    this.io = io;

    setDefaultCommand(setHoodAngle(HoodConstants.minHoodAngle));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/HoodSubsystem", inputs);
  }

  public Command setHoodAngle(Rotation2d desiredAngle) {
    return runOnce(() -> io.setTargetAngle(desiredAngle))
        .andThen(Commands.run(() -> {}, this))
        .until(this::isAtSetpoint);
  }

  @AutoLogOutput
  public boolean isAtSetpoint() {
    return io.isAtSetpoint();
  }

  public Command setVoltage(double volts) {
    return Commands.run(() -> io.setHoodVoltage(volts), this);
  }
}
