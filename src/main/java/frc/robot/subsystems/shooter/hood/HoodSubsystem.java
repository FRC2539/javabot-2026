package frc.robot.subsystems.shooter.hood;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class HoodSubsystem extends SubsystemBase {

  private final HoodIO io;
  private final HoodIOInputsAutoLogged inputs = new HoodIOInputsAutoLogged();

  public HoodSubsystem(HoodIO io) {
    this.io = io;

    //setDefaultCommand(Commands.either(setVoltage(0), setHoodAngle(HoodConstants.minHoodAngle), this::isAtSetpoint));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/HoodSubsystem", inputs);
  }

  public Command setHoodAngle(Supplier<Rotation2d> desiredAngle) {
    return runOnce(() -> io.setTargetAngle(desiredAngle.get()))
        .andThen(Commands.run(() -> {}, this))
        .until(this::isAtSetpoint);
  }

  @AutoLogOutput
  public boolean isAtSetpoint() {
    return io.isAtSetpoint();
  }

  public void setTargetAngle(Supplier<Rotation2d> desiredAngle) {
    io.setTargetAngle(desiredAngle.get());
  }

  public Command setVoltage(double volts) {
    return Commands.run(() -> io.setHoodVoltage(volts), this);
  }
}
