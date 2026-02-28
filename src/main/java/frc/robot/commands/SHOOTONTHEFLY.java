package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;
import frc.robot.subsystems.shooter.turret.TurretSubsystem;

public class SHOOTONTHEFLY extends Command {
    TurretSubsystem turretSubsystem;
    HoodSubsystem hoodSubsystem;
    TargetingSubsystem targetingSubsystem;  
    FlywheelSubsystem flywheelSubsystem;
    IndexerSubsystem indexerSubsystem;

    public SHOOTONTHEFLY(TurretSubsystem turret, HoodSubsystem hood, TargetingSubsystem targeting, FlywheelSubsystem flywheel, IndexerSubsystem indexer) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(turret, hood, targeting, flywheel, indexer);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        turretSubsystem.setTargetAngle(targetingSubsystem.getIdealTurretAngle().get());
        hoodSubsystem.setTargetAngle(targetingSubsystem.getIdealHoodAngle());
        flywheelSubsystem.setShooterRPSForever(targetingSubsystem.getIdealFlywheelRPS().get());

        if (turretSubsystem.isAtSetpoint() && hoodSubsystem.isAtSetpoint() && flywheelSubsystem.atSetpoint()) {
            indexerSubsystem.setVoltages(-9, 12);
        }
    }


}
