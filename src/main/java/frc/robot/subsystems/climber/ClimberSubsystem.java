package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ClimberConstants;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

  private double leftSetpoint = 0.0;
  private double rightSetpoint = 0.0;

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
    if (io != null) io.setBrakeMode(true);
  }

  @Override
  public void periodic() {
    if (io == null) return;

    io.updateInputs(inputs);
    io.setLeftPosition(leftSetpoint);
    io.setRightPosition(rightSetpoint);
  }

  public void setLeftPosition(double rotations) {
    leftSetpoint = Math.max(ClimberConstants.lowerLimitRotations,
                            Math.min(rotations, ClimberConstants.upperLimitRotations));
  }

  public void setRightPosition(double rotations) {
    rightSetpoint = Math.max(ClimberConstants.lowerLimitRotations,
                             Math.min(rotations, ClimberConstants.upperLimitRotations));
  }

  public boolean leftAtSetpoint() {
    return Math.abs(inputs.leftPositionRotations - leftSetpoint) < ClimberConstants.positionTolerance;
  }

  public boolean rightAtSetpoint() {
    return Math.abs(inputs.rightPositionRotations - rightSetpoint) < ClimberConstants.positionTolerance;
  }

//command helpers

public Command moveLeftTo(double rotations) {
    // Pass "this" as the second argument to runOnce and waitUntil to require the subsystem
    return Commands.sequence(
        Commands.runOnce(() -> setLeftPosition(rotations), this),
        Commands.waitUntil(this::leftAtSetpoint) //,this
    );
}

public Command moveRightTo(double rotations) {
    return Commands.sequence(
        Commands.runOnce(() -> setRightPosition(rotations), this),
        Commands.waitUntil(this::rightAtSetpoint) //,this
    );
}


  public Command alternatingClimbCommand() {
    double top = ClimberConstants.upperLimitRotations;
    double bottom = ClimberConstants.lowerLimitRotations;

    return Commands.sequence(
      // 1) Left climber goes up
      moveLeftTo(top),

      // 2) Left pulls while Right goes up
      Commands.parallel(
        moveLeftTo(bottom),
        moveRightTo(top)),

      // 3) Right pulls
      moveRightTo(bottom),

      // 4) Left goes up while Right pulls
      Commands.parallel(
        moveLeftTo(top),
        moveRightTo(bottom)),

      // 5) Left final pull
      moveLeftTo(bottom)
    );
  }

  public Command moveLeftVoltage(double volts) {
    return Commands.run(() -> io.setLeftVoltage(volts), this);
  }

  public Command moveRightVoltage(double volts) {
    return Commands.run(() -> io.setRightVoltage(volts), this);
  }

  public Command stop() {
    return Commands.runOnce(() -> {
      io.setLeftVoltage(0.0);
      io.setRightVoltage(0.0);
    });
  }
}
