package frc.robot.subsystems.roller;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class RollerSubsystem extends SubsystemBase {

  private final RollerIO rollerIO;
  private final RollerIOInputsAutoLogged inputs = new RollerIOInputsAutoLogged();

  public RollerSubsystem(RollerIO rollerIO) {
    this.rollerIO = rollerIO;

    //setDefaultCommand(stopRoller());
  }

  @Override
  public void periodic() {
    rollerIO.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Roller", inputs);
  }

  public Command stopRoller() {
    return Commands.run(() -> rollerIO.setRollerVoltage(0.0), this);
  }

  public Command runPositiveVoltage(double volts) {
    return Commands.run(() -> rollerIO.setRollerVoltage(volts), this);
  }

  public Command runNegativeVoltage(double volts) {
    return Commands.run(() -> rollerIO.setRollerVoltage(-volts), this);
  }

  public Command setVoltage(double volts) {
    return Commands.run(() -> rollerIO.setRollerVoltage(volts), this);
  }
}
