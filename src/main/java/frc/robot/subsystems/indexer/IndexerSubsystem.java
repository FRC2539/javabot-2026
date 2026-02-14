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
      case SHOOT -> io.setVoltages(
          IndexerConstants.indexerMotorShootVoltage, IndexerConstants.transportMotorStartVoltage);
      case REVERSE -> io.setVoltages(
          -IndexerConstants.indexerMotorShootVoltage, -IndexerConstants.transportMotorStartVoltage);
      case STOP -> io.setVoltages(0, 0);
    }
  }

  public Command stop() {
    return Commands.runOnce(() -> goal = Goal.STOP, this);
  }

  public Command index() {
    return Commands.runOnce(() -> goal = Goal.SHOOT, this);
  }

  public Command indexReverse() {
    return Commands.runOnce(() -> goal = Goal.REVERSE, this);
  }

  public enum Goal {
    SHOOT,
    REVERSE,
    STOP
  }
}
