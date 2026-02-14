package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodConstants;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;

public class ShooterCommands {

  public static Command holdToShoot(
      FlywheelSubsystem flywheel,
      HoodSubsystem hood,
      IndexerSubsystem indexer,
      TargetingSubsystem targeting) {

    return Commands.startEnd(
            () -> {
              // Spin flywheel
              flywheel.setShooterRPM(targeting.getIdealFlywheelRPM()).schedule();

              // Move hood
              hood.setHoodAngle(targeting.getIdealHoodAngle()).schedule();
            },
            () -> {
              indexer.stop().schedule();

              // Return shooter
              flywheel.setShooterRPM(0).schedule();

              // Return hood
              hood.setHoodAngle(Rotation2d.fromRadians(HoodConstants.kMinAngleRad)).schedule();
            })
        // held
        .alongWith(
            Commands.waitUntil(() -> flywheel.atSetpoint() && hood.isAtSetpoint())
                .andThen(indexer.index()));
  }
}
