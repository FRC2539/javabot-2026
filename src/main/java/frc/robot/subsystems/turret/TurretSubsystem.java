package frc.robot.subsystems.turret;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public TurretSubsystem() {
    }
    
//#region Internal Data
    // TODO: Read absolute rotation of the turret
    private static DoubleSupplier rotation = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    // TODO: Read the speed of the indexer 
    private static DoubleSupplier indexerSpeed = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    // TODO: Read the speed of the shooter
    private static DoubleSupplier shooterSpeed = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    // TODO: Read a sensor in the turret
    private static BooleanSupplier indexerHasPiece = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return false; });
    // TODO: Read a sensor by the indexer
    private static BooleanSupplier shooterHasPiece = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return false; });
    
    // TODO: Calculate the ideal absolute rotation of the turret to score
    private static DoubleSupplier targetRotation = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
    // TODO: Calculate the ideal speed of the turret to score
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

        Inactive, // Not considering aiming
        Moving, // Moving to shooting position
        Ready // Currently aligned with target, within tolerence
    }

    public static double Rotation() { return rotation.getAsDouble(); }
    
    // Course data
    public static ShootingStatus ShootingStatus;
    public static AimingStatus AimingStatus;

    // Fine data 
    public static double IndexerSpeed() { return indexerSpeed.getAsDouble(); }
    public static double ShooterSpeed() { return shooterSpeed.getAsDouble(); }
    public static boolean IndexerHasPiece() { return indexerHasPiece.getAsBoolean(); }
    public static boolean ShooterHasPiece() { return shooterHasPiece.getAsBoolean(); }
//#endregion


    

//#region Internal Controls
    // TODO: Prepare for, then engage in shots continuously while allowed. Should ideally complete or reintake the last shot before shutting down
    public Command shoot() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });        
    }
    // TODO: Get the shooter to a constant speed
    public Command shooterSpinUp() {
        // Uses targetSpeed supplier
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Follow a target rotation, calcualted using 
    public Command followTarget() {
        // Uses targetRotation supplier
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion


//#region External Controls
    // TODO: Engage shooting on command, and disengage when finished
    public static Command Shoot() {
        // Calls stopShooting() when command is ended
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }

    // TODO: Allow changes to targetRotation and targetSpeed supplier calculations
    public static void ChangeSpeedOffset(double changeBy) {

    }
    public static void ChangeRotationOffset(double changeBy) {

    }
//#endregion
}
