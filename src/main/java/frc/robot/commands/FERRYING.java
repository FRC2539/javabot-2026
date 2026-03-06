package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;

public class FERRYING extends Command {
  TurretSubsystem turretSubsystem;
  HoodSubsystem hoodSubsystem;
  TargetingSubsystem targetingSubsystem;
  FlywheelSubsystem flywheelSubsystem;
  IndexerSubsystem indexerSubsystem;
  boolean ferryingLeft;

  public boolean hasSpunUp = false;
  public FERRYING(
      TurretSubsystem turret,
      HoodSubsystem hood,
      TargetingSubsystem targeting,
      FlywheelSubsystem flywheel,
      IndexerSubsystem indexer,
      boolean fL) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turret, hood, targeting, flywheel, indexer);
    turretSubsystem = turret;
    hoodSubsystem = hood;
    targetingSubsystem = targeting;
    flywheelSubsystem = flywheel;
    indexerSubsystem = indexer;
    ferryingLeft = fL;
  }

  @Override
  public void initialize() {
    targetingSubsystem.isFerrying(true);

    targetingSubsystem.ferryingLeft = ferryingLeft;
  }

  @Override
  public void execute() {

      turretSubsystem.setTargetAngle(targetingSubsystem.getIdealTurretAngle().get());
      hoodSubsystem.setTargetAngle(targetingSubsystem.getIdealHoodAngle());
      flywheelSubsystem.setTargetRPS(targetingSubsystem.getIdealFlywheelRPS().get());
      //System.out.println(
        //   "flywheel "
        //       + targetingSubsystem.getIdealFlywheelRPS().get()
        //       + "hood "
        //       + targetingSubsystem.getIdealHoodAngle().get().getDegrees()
        //       + "turret "
        //       + targetingSubsystem.getIdealTurretAngle().get().getDegrees());

        if (flywheelSubsystem.atSetpoint()) {
            hasSpunUp = true;
        }
      if (turretSubsystem.isAtSetpoint()
          && hoodSubsystem.isAtSetpoint()
          && (flywheelSubsystem.atSetpoint() || hasSpunUp)) {
        indexerSubsystem.setVoltagesFunction(-12, 12);
      }
    
  }
}
