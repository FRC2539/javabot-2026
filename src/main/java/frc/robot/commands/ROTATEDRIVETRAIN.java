package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;
import frc.robot.subsystems.shooter.turret.TurretConstants;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;

public class ROTATEDRIVETRAIN extends Command {
  HoodSubsystem hoodSubsystem;
  TargetingSubsystem targetingSubsystem;
  FlywheelSubsystem flywheelSubsystem;
  IndexerSubsystem indexerSubsystem;
  CommandSwerveDrivetrain dt;

    private final SwerveRequest.FieldCentric driveRequest =
      new SwerveRequest.FieldCentric()
          .withDriveRequestType(DriveRequestType.Velocity);

  PIDController thetaController = new PIDController(25, 0, 0.03);

  public boolean hasSpunUp = false;

  public ROTATEDRIVETRAIN(
      HoodSubsystem hood,
      TargetingSubsystem targeting,
      FlywheelSubsystem flywheel,
      IndexerSubsystem indexer,
      CommandSwerveDrivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(hood, targeting, flywheel, indexer, drivetrain);
    hoodSubsystem = hood;
    targetingSubsystem = targeting;
    flywheelSubsystem = flywheel;
    indexerSubsystem = indexer;
    dt = drivetrain;
  }

  @Override
  public void initialize() {
    targetingSubsystem.isFerrying(false);
    thetaController.setTolerance(Units.degreesToRotations(3.5));
    thetaController.enableContinuousInput(Units.degreesToRotations(-180), Units.degreesToRotations(180));
    thetaController.setSetpoint(targetingSubsystem.getIdealTurretAngle().get().getRotations());
  }

  @Override
  public void execute() {

    hoodSubsystem.setTargetAngle(targetingSubsystem.getIdealHoodAngle());
    flywheelSubsystem.setTargetRPS(targetingSubsystem.getIdealFlywheelRPS().get());
    // System.out.println(
    //   "flywheel "
    //       + targetingSubsystem.getIdealFlywheelRPS().get()
    //       + "hood "
    //       + targetingSubsystem.getIdealHoodAngle().get().getDegrees()
    //       + "turret "
    //       + targetingSubsystem.getIdealTurretAngle().get().getDegrees());


    double currentHeadingRot =  MathUtil.inputModulus(
            dt.getHeading().getRotations(),
            -.5,
            .5);
    double desiredRotationalRate = thetaController.calculate(currentHeadingRot, (targetingSubsystem.getIdealTurretAngle()).get().getRotations());

    System.out.println(desiredRotationalRate);
    dt.setControl(driveRequest.withRotationalRate(desiredRotationalRate));

    if (flywheelSubsystem.atSetpoint()) {
      hasSpunUp = true;
    }
    if (thetaController.atSetpoint()
        && hoodSubsystem.isAtSetpoint()
        && (flywheelSubsystem.atSetpoint() || hasSpunUp)) {
      indexerSubsystem.setVoltagesFunction(-12, 12);
    }
  }
}
