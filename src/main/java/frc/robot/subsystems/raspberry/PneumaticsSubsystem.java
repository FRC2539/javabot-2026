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

  public Command setRaspberryPosition(raspberryPosition position) {
    return runOnce(() -> io.setRaspberrySolenoid(position.value));
  }

  public Command setRaspberry2Position(raspberry2Position position) {
    return runOnce(() -> io.setRaspberry2Solenoid(position.value));
  }

  // public Command toggleIntake() {
  //   return runOnce(
  //       () -> {
  //         if (inputs.intakeState == IntakePosition.DEPLOYED.value) {
  //           io.setIntakeSolenoid(IntakePosition.RETRACTED.value);
  //         } else {
  //           io.setIntakeSolenoid(IntakePosition.DEPLOYED.value);
  //         }
  //       });
  // }

  public Command toggleIntake() {
    return runOnce(
        () -> {
          if (inputs.raspberry2 != raspberry2Position.EXPANDED.value) {
            io.setIntakeSolenoid(IntakePosition.RETRACTED.value);
            return;
          }

          if (inputs.intakeState == IntakePosition.DEPLOYED.value) {
            io.setIntakeSolenoid(IntakePosition.RETRACTED.value);
          } else {
            io.setIntakeSolenoid(IntakePosition.DEPLOYED.value);
          }
        });
  }

  public Command setRaspberry2Position(PneumaticPosition position) {
    return runOnce(() -> io.setRaspberry2Solenoid(position.value));
  }

  // public Command toggleRaspberry2() {
  //   return runOnce(
  //       () -> {
  //         if (inputs.raspberry2 == raspberry2Position.EXPANDED.value) {
  //           io.setRaspberry2Solenoid(raspberry2Position.RETRACTED.value);
  //         } else {
  //           io.setRaspberry2Solenoid(raspberry2Position.EXPANDED.value);
  //         }
  //       });
  // }

  public Command toggleRaspberry2() {
    return runOnce(
        () -> {
          if (inputs.raspberry2 == raspberry2Position.EXPANDED.value) {

            io.setRaspberry2Solenoid(raspberry2Position.RETRACTED.value);

            io.setIntakeSolenoid(IntakePosition.RETRACTED.value);

          } else {
            io.setRaspberry2Solenoid(raspberry2Position.EXPANDED.value);
          }
        });
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Pneumatics", inputs);

    SmartDashboard.putNumber("Pneumatics Pressure (PSI)", inputs.pressurePsi);
  }
}
