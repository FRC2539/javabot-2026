package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import frc.robot.util.LoggedTunableNumber;

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
    Logger.processInputs("Indexer", inputs.indexerMotorVoltage);
    Logger.processInputs("Transport", inputs.transportMotorVoltage);

    switch (goal) {
      case SHOOT -> io.setVoltages(IndexerConstants.indexerMotorShootVoltage, IndexerConstants.transportMotorStartVoltage);
      case REVERSE -> io.setVoltages(IndexerConstants.indexerMotorReverseVoltage, IndexerConstants.transportMotorReverseVoltage);
      case STOP -> io.setVoltages(0, 0);
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

  public enum Goal {
    SHOOT,
    REVERSE,
    STOP
  }
}
