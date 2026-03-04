package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Supplier;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class TurretSubsystem extends SubsystemBase {

  private final TurretIO io;
  private final TurretIOInputsAutoLogged inputs = new TurretIOInputsAutoLogged();

  public TurretSubsystem(TurretIO io) {
    this.io = io;
    setDefaultCommand(setVoltage(0));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("RealOutputs/TurretSubsystem", inputs);
  }

  public boolean atTarget() {
    return io.isAtSetpoint();
  }

  public Command goToAngleCommand(Supplier<Rotation2d> angle) {
    return Commands.runOnce(() -> this.setTargetAngle(angle.get()), this)
        .andThen(Commands.run(() -> {}, this))
        .until(this::isAtSetpoint);
  }

  public void setTargetAngle(Rotation2d angle) {

    Rotation2d mechanicalTarget = angle.plus(Rotation2d.fromRotations(0.145508)).plus(Rotation2d.fromRotations(.03));
    // .plus(Rotation2d.fromRotations(0.1549902))
    // .plus(Rotation2d.fromDegrees(30))
    // .minus(Rotation2d.fromRotations(0.04117))
    // .minus(Rotation2d.fromRotations(0.061034));

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

  @AutoLogOutput
  public Rotation2d currentTurretHeading() {
    return Rotation2d.fromDegrees(inputs.positionDeg)
        .plus(Rotation2d.fromRotations(0.145508).plus(Rotation2d.fromRotations(.03))); // .minus(Rotation2d.fromRotations(0.1549902));
  }
}
