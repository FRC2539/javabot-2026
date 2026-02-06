package frc.robot.subsystems.transporter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.Logger;

public class TransportSubsystem extends SubsystemBase {

  private static final LoggedTunableNumber setTransportVoltage =
      new LoggedTunableNumber("/transport/TransportVoltage", 12.0);

  private final TransportIO transportIO;
  private final TransportIO.TransportIOInputs transportInputs = new TransportIO.TransportIOInputs();

  public TransportSubsystem(TransportIO transportIO) {
    this.transportIO = transportIO;
    setDefaultCommand(setTransportVoltageCommand(0.0));
  }

  @Override
  public void periodic() {
    transportIO.updateInputs(transportInputs);
    Logger.processInputs("RealOutputs/Transport", transportInputs);
  }

  // helpers
  public Command setTransportVoltageCommand(double volts) {
    return Commands.run(() -> transportIO.setTransportVoltage(volts), this);
  }
}
