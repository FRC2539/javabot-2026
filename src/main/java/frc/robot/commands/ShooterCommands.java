package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;

public class ShooterCommands {

  public static Command holdToShoot(
      FlywheelSubsystem flywheel,
      HoodSubsystem hood,
      IndexerSubsystem indexer,
      TargetingSubsystem targeting) {

    return Commands.sequence(
            Commands.parallel(
                flywheel.setShooterRPMCommand(targeting.getIdealFlywheelRPM()),
                hood.setHoodAngle(targeting.getIdealHoodAngle())),
            indexer.indexToShooter());
        // .finallyDo(
        //     interrupted -> {
        //       indexer.stop();
        //       flywheel.setShooterRPMCommand(0);
        //     });
  }
}
