package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
    io.setBrakeMode(true);

    setDefaultCommand(stopClimber());
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("ClimberSubsystem", inputs);
  }

  public Command stopClimber() {
    return Commands.run(() -> io.setVoltage(0.0), this);
  }

  public Command setVoltage(double voltage) {
    return Commands.run(() -> io.setVoltage(voltage), this);
  }
}
