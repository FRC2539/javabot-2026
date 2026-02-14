package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class IndexerSubsystem extends SubsystemBase {

  private final IndexerIO io;
  private final IndexerIOInputsAutoLogged inputs = new IndexerIOInputsAutoLogged();

  @AutoLogOutput private Goal goal = Goal.STOP;

  public IndexerSubsystem(IndexerIO io) {
    this.io = io;
    setDefaultCommand(stop());
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Indexer", inputs);

    switch (goal) {
      case SHOOT -> io.setVoltage(IndexerConstants.shootVolts.get());
      case REVERSE -> io.setVoltage(IndexerConstants.reverseVolts.get());
      case STOP -> io.setVoltage(IndexerConstants.stopVolts.get());
    }
  }

  public Command stop() {
    return Commands.run(() -> goal = Goal.STOP, this);
  }

  public Command runPositive() {
    return Commands.run(() -> goal = Goal.SHOOT, this);
  }

  public Command runNegative() {
    return Commands.run(() -> goal = Goal.REVERSE, this);
  }

  public Command setVoltage(double voltage) {
    return Commands.run(() -> io.setVoltage(voltage), this);
  }

  public enum Goal {
    SHOOT,
    REVERSE,
    STOP
  }
}
