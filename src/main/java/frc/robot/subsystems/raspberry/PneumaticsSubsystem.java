package frc.robot.subsystems.raspberry;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class PneumaticsSubsystem extends SubsystemBase {

  private final PneumaticsIO io;
  private final PneumaticsIOInputsAutoLogged inputs =
      new PneumaticsIOInputsAutoLogged();

  public PneumaticsSubsystem(PneumaticsIO io) {
    this.io = io;
  }

  public enum IntakePosition {
    DEPLOYED(Value.kForward),
    RETRACTED(Value.kReverse),
    OFF(Value.kOff);

    public final Value value;
    IntakePosition(Value value) {
      this.value = value;
    }
  }

  public enum raspberryPosition {
    EXPANDED(Value.kForward),
    RETRACTED(Value.kReverse),
    OFF(Value.kOff);

    public final Value value;
    raspberryPosition(Value value) {
      this.value = value;
    }
  }

  public enum raspberry2Position {
    EXPANDED(Value.kForward),
    RETRACTED(Value.kReverse),
    OFF(Value.kOff);

    public final Value value;
    raspberry2Position(Value value) {
      this.value = value;
    }
  }


  public Command setIntakePosition(IntakePosition position) {
    return runOnce(() -> io.setIntakeSolenoid(position.value));
  }

  public Command setRaspberryPosition(raspberryPosition position) {
    return runOnce(() -> io.setRaspberrySolenoid(position.value));
  }

  public Command setRaspberry2Position(raspberry2Position position) {
    return runOnce(() -> io.setRaspberry2Solenoid(position.value));
  }


  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Pneumatics", inputs);
  }
}