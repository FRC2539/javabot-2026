package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs =
      new ClimberIOInputsAutoLogged();

  private static final LoggedTunableNumber climbVoltage =
      new LoggedTunableNumber("/climber/ClimbVoltage", 8.0);

  private static final LoggedTunableNumber tolerance =
      new LoggedTunableNumber("/climber/Tolerance", 0.5);

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
  }

  public enum ClimbPosition {
    STOWED(0.0),
    MID(35.0),
    CLIMB(70.0);

    public final double rotations;

    ClimbPosition(double rotations) {
      this.rotations = rotations;
    }
  }

  private boolean leftAt(double pos) {
    return Math.abs(inputs.leftPosition - pos) < tolerance.get();
  }

  private boolean rightAt(double pos) {
    return Math.abs(inputs.rightPosition - pos) < tolerance.get();
  }

  private void driveLeft(double target) {
    io.setLeftVoltage(
        Math.signum(target - inputs.leftPosition) * climbVoltage.get());
  }

  private void driveRight(double target) {
    io.setRightVoltage(
        Math.signum(target - inputs.rightPosition) * climbVoltage.get());
  }

  private void stop() {
    io.setLeftVoltage(0);
    io.setRightVoltage(0);
  }



  private Command moveLeftTo(double target) {
    return Commands.run(() -> driveLeft(target), this)
        .until(() -> leftAt(target))
        .andThen(() -> io.setLeftVoltage(0));
  }

  private Command moveRightTo(double target) {
    return Commands.run(() -> driveRight(target), this)
        .until(() -> rightAt(target))
        .andThen(() -> io.setRightVoltage(0));
  }


  public Command climbToPosition(ClimbPosition position) {
    return Commands.sequence(
        moveLeftTo(position.rotations),
        moveRightTo(position.rotations),
        moveLeftTo(position.rotations));
  }

  public Command moveUpVoltage(double volts) {
    return run(() -> {
      io.setLeftVoltage(volts);
      io.setRightVoltage(volts);
    });
  }

  public Command moveDownVoltage(double volts) {
    return run(() -> {
      io.setLeftVoltage(-volts);
      io.setRightVoltage(-volts);
    });
  }

  public Command stopCommand() {
    return runOnce(this::stop);
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/Climber", inputs);
  }
}
