package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class IndexerSubsystem extends SubsystemBase {

  private final IndexerIO io;
  private final IndexerIOInputsAutoLogged inputs =
      new IndexerIOInputsAutoLogged();

  public IndexerSubsystem(IndexerIO io) {
    this.io = io;

    setDefaultCommand(stop());
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Indexer", inputs);
  }

  public Command runPositive() {
    return Commands.run(
        () -> io.setVoltage(IndexerConstants.shootVolts.get()),
        this);
  }

  public Command runNegative() {
    return Commands.run(
        () -> io.setVoltage(IndexerConstants.reverseVolts.get()),
        this);
  }

  public Command stop() {
    return Commands.run(
        () -> io.setVoltage(IndexerConstants.stopVolts.get()),
        this);
  }

  public Command setVoltage(double voltage) {
    return Commands.run(
        () -> io.setVoltage(voltage),
        this);
  }
}
