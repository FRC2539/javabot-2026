package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class TurretSubsystem extends SubsystemBase {

  private final TurretIO io;
  private final TurretIOInputsAutoLogged inputs = new TurretIOInputsAutoLogged();

  public TurretSubsystem(TurretIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/TurretSubsystem", inputs);
  }

  public boolean atTarget() {
    return io.isAtSetpoint();
  }

  public Command goToAngleCommand(Rotation2d angle) {
    return Commands.runOnce(() -> this.setTargetAngle(angle), this)
        .andThen(Commands.run(() -> {}, this))
        .until(this::isAtSetpoint);
  }

  public void setTargetAngle(Rotation2d angle) {

    Rotation2d mechanicalTarget = angle.plus(Rotation2d.fromRotations(0.155029));

    double wrappedRotationDeg =
        MathUtil.inputModulus(
            mechanicalTarget.getDegrees(),
            TurretConstants.minAngle.getDegrees(),
            TurretConstants.maxAngle.getDegrees());

    wrappedRotationDeg =
        MathUtil.clamp(
            wrappedRotationDeg,
            TurretConstants.minAngle.getDegrees(),
            TurretConstants.maxAngle.getDegrees());

    io.setTargetHeading(Rotation2d.fromDegrees(wrappedRotationDeg));
  }

  public boolean isAtSetpoint() {
    return io.isAtSetpoint();
  }

  public Command setVoltage(double volts) {
    return Commands.run(() -> io.setVoltage(volts), this);
  }
}
