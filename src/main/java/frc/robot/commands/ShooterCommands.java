package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;

public class ShooterCommands {

  public static Command holdToShoot(
      FlywheelSubsystem flywheel,
      HoodSubsystem hood,
      IndexerSubsystem indexer,
      TargetingSubsystem targeting) {

    return Commands.sequence(
        Commands.parallel(
            flywheel.setShooterRPSCommand(targeting.getIdealFlywheelRPM() / 60),
            hood.setHoodAngle(targeting.getIdealHoodAngle())),
        indexer.indexToShooter());
    // .finallyDo(
    //     interrupted -> {
    //       indexer.stop();
    //       flywheel.setShooterRPMCommand(0);
    //     });
  }

  public static Command shootWheels(FlywheelSubsystem flywheel, IndexerSubsystem indexer, double shooterRPS) {

    // return Commands.parallel(flywheel.setVoltage(8), indexer.indexToShooter());

    return Commands.sequence(flywheel.setShooterRPSCommand(shooterRPS), indexer.indexToShooter());
  }


  public static Command HubShot(FlywheelSubsystem flywheel, IndexerSubsystem indexer, TurretSubsystem turret, HoodSubsystem hood, double shooterRPS) {
    return Commands.sequence(
        Commands.parallel(
            flywheel.setShooterRPSCommand(shooterRPS),
            Commands.sequence(hood.setHoodAngle(Rotation2d.fromRotations(0.05273)), hood.setVoltage(0)).withTimeout(0.35),
            turret.goToAngleCommand(Rotation2d.fromRotations(0.1503))),
        indexer.indexToShooter());
  }
}
