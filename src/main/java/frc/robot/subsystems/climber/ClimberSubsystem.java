package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ClimberConstants;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs =
      new ClimberIOInputsAutoLogged();

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
    io.setBrakeMode(true);
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Climber", inputs);
  }


  public void setPosition(double positionRot) {
    io.setPosition(positionRot);
  }

  public void setVoltage(double volts) {
    io.setVoltage(volts);
  }

  public void stop() {
    io.stop();
  }

  public boolean atPosition(double targetRot) {
    return Math.abs(inputs.positionRot - targetRot)
        <= ClimberConstants.kTolerance;
  }

  public Command climbSequence() {
    return this.runOnce(() -> {})
        .andThen(moveTo(ClimberConstants.kUpPosition))
        .andThen(moveTo(ClimberConstants.kDownPosition))
        .andThen(moveTo(ClimberConstants.kUpPosition))
        .andThen(moveTo(ClimberConstants.kDownPosition))
        .andThen(moveTo(ClimberConstants.kUpPosition))
        .andThen(moveTo(ClimberConstants.kDownPosition));
  }

  private Command moveTo(double positionRot) {
    return this.run(() -> setPosition(positionRot))
        .until(() -> atPosition(positionRot))
        .finallyDo(this::stop);
  }
}
