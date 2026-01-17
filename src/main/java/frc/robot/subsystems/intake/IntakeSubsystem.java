package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.BooleanSupplier;

public class IntakeSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    static boolean hasInit;
    public IntakeSubsystem() {
        // TODO: Initialize "hasPiece" supplier; Read the sensor on the intake
    }
    
//#region Internal Data
    private static BooleanSupplier hasPiece = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return false; });
//#endregion


//#region Shared Data
    public enum Position {
        INVALID,
        
        Stowed,
        Moving,
        Ready
    }
    public enum Status {
        INVALID,
        
        Waiting,
        Intake,
        Extake
    }
    
    public static boolean HasPiece() { return hasPiece.getAsBoolean(); }

    public static Position CurrentPosition = Position.INVALID;
    public static Position TargetPosition = Position.INVALID;
    public static Status CurrentStatus = Status.INVALID;
//#endregion



//#region Internal Controls
    @Override
    public void periodic() {}

    // TODO: Stop the rollers
    private static Command stopSpinning() {
        // Command ends when rollers stop moving
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Start the rollers
    private static Command startSpinningIntake() {
        // Command ends when rollers are up to speed
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Start the rollers in reverse
    private static Command startSpinningExtake() {
        // Command ends when rollers are up to speed
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }

    // TODO: Moves the intake to the desired position
    private static Command moveToPosition(Position position) {
        // Command ends when intake is at the desired position
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion



//#region External Controls
    // TODO: Stow the intake using internal controls
    public static Command Stow() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    } 
    // TODO: Engage the intake using internal controls
    public static Command Engage() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion
}
