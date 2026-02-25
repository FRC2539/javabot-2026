package frc.robot.subsystems.raspberry;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class PneumaticsSubsystem extends SubsystemBase {

  private final PneumaticsIO io;
  private final PneumaticsIOInputsAutoLogged inputs = new PneumaticsIOInputsAutoLogged();

  public PneumaticsSubsystem(PneumaticsIO io) {
    this.io = io;
  }

  public enum PneumaticPosition {
    FORWARD(Value.kForward),
    REVERSE(Value.kReverse),
    OFF(Value.kOff);

    public final Value value;

    PneumaticPosition(Value value) {
      this.value = value;
    }
  }

  public Command setIntakePosition(PneumaticPosition position) {
    return runOnce(() -> io.setIntakeSolenoid(position.value));
  }

  public Command toggleIntake() {
    return Commands.either(
        setIntakePosition(PneumaticPosition.REVERSE),
        setIntakePosition(PneumaticPosition.FORWARD),
        () -> inputs.intakeState == Value.kForward);
  }

  public Command setRaspberry2Position(PneumaticPosition position) {
    return runOnce(() -> io.setRaspberry2Solenoid(position.value));
  }

  public Command toggleRaspberry2() {
    return Commands.either(
        setRaspberry2Position(PneumaticPosition.REVERSE),
        setRaspberry2Position(PneumaticPosition.FORWARD),
        () -> inputs.raspberry2 == Value.kForward);
  }

  public Command dropIntakeRaspberry2Deployed() {
    return Commands.sequence(
        Commands.either(
            Commands.none(),
            setRaspberry2Position(PneumaticPosition.FORWARD),
            () -> inputs.raspberry2 == Value.kForward),
        setIntakePosition(PneumaticPosition.FORWARD));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Pneumatics", inputs);

    SmartDashboard.putNumber("Pneumatics Pressure (PSI)", inputs.pressurePsi);
  }
}
