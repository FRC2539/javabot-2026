package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.IndexerSubsystem;
import frc.robot.subsystems.shooter.flywheel.FlywheelSubsystem;
import frc.robot.subsystems.shooter.hood.HoodSubsystem;
import frc.robot.subsystems.shooter.targeting.TargetingSubsystem;
import frc.robot.subsystems.shooter.turret.TurretConstants;
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
        turretSubsystem = turret;
        hoodSubsystem = hood;
        targetingSubsystem = targeting;
        flywheelSubsystem = flywheel;
        indexerSubsystem = indexer;
        
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Rotation2d turretAngle = targetingSubsystem.getIdealTurretAngle().get();
        Rotation2d mechanicalTarget = turretAngle.plus(Rotation2d.fromRotations(0.155029));

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
        if (wrappedRotationDeg >= TurretConstants.maxAngle.getDegrees() || wrappedRotationDeg <= TurretConstants.minAngle.getDegrees()) {

        } else {
            turretSubsystem.setTargetAngle(targetingSubsystem.getIdealTurretAngle().get());
            hoodSubsystem.setTargetAngle(targetingSubsystem.getIdealHoodAngle());
            flywheelSubsystem.setTargetRPS(targetingSubsystem.getIdealFlywheelRPS().get());
            System.out.println("flywheel " + targetingSubsystem.getIdealFlywheelRPS().get() + "hood " + targetingSubsystem.getIdealHoodAngle().get().getDegrees() + "turret " + targetingSubsystem.getIdealTurretAngle().get().getDegrees());
            if (turretSubsystem.isAtSetpoint() && hoodSubsystem.isAtSetpoint() && flywheelSubsystem.atSetpoint()) {
                indexerSubsystem.setVoltagesFunction(-9, 12);
            }
        }

   
       
        

        
    }


}
