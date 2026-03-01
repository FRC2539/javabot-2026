package frc.robot.subsystems.indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class IndexerSubsystem extends SubsystemBase {

  private final IndexerIO io;
  private final IndexerIOInputsAutoLogged inputs = new IndexerIOInputsAutoLogged();

  public IndexerSubsystem(IndexerIO io) {
    this.io = io;

    setDefaultCommand(setVoltages(0, 0));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Indexer", inputs);
  }

  public Command setVoltages(double indexerVoltage, double transportVoltage) {
    return Commands.run(() -> io.setVoltages(indexerVoltage, transportVoltage), this);
  }

  public void setVoltagesFunction(double indexerVoltage, double transportVoltage) {
    io.setVoltages(indexerVoltage, transportVoltage);
  }

  public Command indexToShooter() {
    return setVoltages(
        IndexerConstants.indexerShootVoltage, IndexerConstants.transportShootVoltage);
  }
}
