package frc.robot.subsystems.turret;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public TurretSubsystem() {
        // TODO: Initialize "turretRotation" supplier; Read absolute rotation of the turret
        // TODO: Initialize "indexerSpeed" supplier; Read the speed of the indexer 
        // TODO: Initialize "shooterSpeed" supplier; Read the speed of the shooter
        // TODO: Initialize "indexerHasPiece" supplier; Read a sensor in the turret
        // TODO: Initialize "shooterHasPiece" supplier; Read a sensor by the indexer

        // TODO: Initialize "targetRotation" supplier; Calculate the ideal absolute rotation of the turret to score
        // TODO: Initialize "targetSpeed" supplier; Calculate the ideal speed of the turret to score
    }
    
//#region Internal Data
    private static DoubleSupplier turretRotation = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    private static DoubleSupplier indexerSpeed = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    private static DoubleSupplier shooterSpeed = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    private static BooleanSupplier indexerHasPiece = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return false; });
    private static BooleanSupplier shooterHasPiece = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return false; });

    // Targeting
    private static DoubleSupplier targetRotation = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    private static DoubleSupplier targetSpeed = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
//#endregion


//#region Shared Data
    public enum ShootingStatus {
        INVALID,

        Inactive, // Not spinning
        Accelerating, // Has not built up enough speed
        Ready, // Up to speed, ready to shoot
        Engaged // Currently shooting a piece
    }
    public enum AimingStatus {
        INVALID,

        Inactive, // Ignoring aim
        Moving, // Moving to shooting position
        Ready // Currently aligned with target, within tolerence, ready to shoot
    }
    
    // Course data
    public static ShootingStatus TurretShootingStatus;
    public static AimingStatus TurretAimingStatus;
    
    // Fine data 
    public static double TurretRotation() { return turretRotation.getAsDouble(); }
    public static double IndexerSpeed() { return indexerSpeed.getAsDouble(); }
    public static double ShooterSpeed() { return shooterSpeed.getAsDouble(); }
    public static boolean IndexerHasPiece() { return indexerHasPiece.getAsBoolean(); }
    public static boolean ShooterHasPiece() { return shooterHasPiece.getAsBoolean(); }

    public static double TargetRotation() { return targetRotation.getAsDouble(); }
    public static double TargetSpeed() { return targetSpeed.getAsDouble(); }
//#endregion


    

//#region Internal Controls
    // TODO: Get the shooter to a calculated speed
    private static Command spinUp() {
        // Uses targetSpeed supplier
        // Updates ShootingStatus
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Follow a calcualted target rotation  
    private static Command followTarget() {
        // Uses targetRotation supplier
        // Updates AimingStatus
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion


//#region External Controls
    // TODO: Prepare for a shot, and hold
    public static Command Prepare() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Prepare for, then engage in shots continuously while allowed. Should ideally finish or reintake the last shot before ending
    public static Command Shoot() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Allow changes to targetRotation and targetSpeed supplier calculations
    public static void ChangeSpeedOffset(double changeBy) {
        if (AllowUnimplementedErrors) System.err.println("Function not implemented");
    }
    public static void ChangeRotationOffset(double changeBy) {
        if (AllowUnimplementedErrors) System.err.println("Function not implemented");
    }
//#endregion
}
