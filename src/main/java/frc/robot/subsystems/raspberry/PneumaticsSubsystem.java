package frc.robot.subsystems.raspberry;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.raspberry.PneumaticsSubsystem.IntakePosition;
import org.littletonrobotics.junction.Logger;

public class PneumaticsSubsystem extends SubsystemBase {

  private final PneumaticsIO io;
  private final PneumaticsIOInputsAutoLogged inputs = new PneumaticsIOInputsAutoLogged();

  private final PneumaticHub pneumaticHub = new PneumaticHub(PneumaticsConstants.pneumaticHubId);

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

  // public enum raspberryPosition {
  //   EXPANDED(Value.kForward),
  //   RETRACTED(Value.kReverse),
  //   OFF(Value.kOff);

  //   public final Value value;

  //   raspberryPosition(Value value) {
  //     this.value = value;
  //   }
  // }

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

  // public Command setRaspberryPosition(raspberryPosition position) {
  //   return runOnce(() -> io.setRaspberrySolenoid(position.value));
  // }

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
    return Commands.either(
        setIntakePosition(IntakePosition.RETRACTED),
        setIntakePosition(IntakePosition.DEPLOYED),
        () -> inputs.intakeState == IntakePosition.DEPLOYED.value);
  }

  // public Command toggleRaspberry() {
  //   return runOnce(
  //       () -> {
  //         if (inputs.raspberry == raspberryPosition.EXPANDED.value) {
  //           io.setRaspberrySolenoid(raspberryPosition.RETRACTED.value);
  //         } else {
  //           io.setRaspberrySolenoid(raspberryPosition.EXPANDED.value);
  //         }
  //       });
  // }

  // public Command toggleRaspberry() {
  //   return Commands.either(
  //       setRaspberryPosition(raspberryPosition.RETRACTED),
  //       setRaspberryPosition(raspberryPosition.EXPANDED),
  //       () -> inputs.raspberry == raspberryPosition.EXPANDED.value);
  // }

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
    return Commands.either(
        setRaspberry2Position(raspberry2Position.RETRACTED),
        setRaspberry2Position(raspberry2Position.EXPANDED),
        () -> inputs.raspberry2 == raspberry2Position.EXPANDED.value);
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Pneumatics", inputs);
    // gets pressure in psi
    double pressure = pneumaticHub.getPressure(0);

    SmartDashboard.putNumber("Pneumatics Pressure (PSI)", pressure);
  }
}
