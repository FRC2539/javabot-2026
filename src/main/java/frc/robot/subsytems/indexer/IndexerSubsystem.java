package frc.robot.subsytems.indexer;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {

  IndexerIO indexerIO;
  IndexerIOInputsAutoLogged indexerInputs = new IndexerIOInputsAutoLogged();

  public IndexerSubsystem(IndexerIO indexerIO) {
    this.indexerIO = indexerIO;
  }

  @Override
  public void periodic() {
    indexerIO.updateInputs(indexerInputs);
    Logger.processInputs("RealOutputs/Indexer", indexerInputs);
  }

  public Command setVoltage(double voltage) {
    return Commands.run(() -> indexerIO.setVoltage(voltage));
  }

  public Command indexToShooter() {
    return setVoltage(IndexerConstants.indexToShooterVoltage);
  }

}
