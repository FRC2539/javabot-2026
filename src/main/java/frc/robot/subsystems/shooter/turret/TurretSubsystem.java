package frc.robot.subsystems.shooter.turret;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.TurretConstants;

public class TurretSubsystem extends SubsystemBase {

  private final TurretIO io;
  private final TurretIO.TurretIOInputs inputs = new TurretIO.TurretIOInputs();

  private double targetRad = 0.0;

  public TurretSubsystem(TurretIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
  }

  public void aimAtTarget(Pose2d hubPose, Pose2d robotPose, ChassisSpeeds speeds) {

    targetRad =
        MathUtil.clamp(
            TurretSolver.solveTurretAngle(hubPose, robotPose, speeds),
            TurretConstants.MinRadAngle,
            TurretConstants.MaxRadAngle);

    io.setPositionRad(targetRad);
  }

  public void setVoltage(double volts) {
    io.setVoltage(volts);
  }

  public boolean atTarget() {
    return Math.abs(inputs.positionRad - targetRad) <= TurretConstants.RadTolerance;
  }

  public void stop() {
    io.stop();
  }
}
