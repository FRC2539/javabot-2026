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
        Stowed,
        Moving,
        Ready
    }
    public enum Status {
        Waiting,
        Intake,
        Extake
    }
    
    public static boolean HasPiece() { return hasPiece.getAsBoolean(); }

    public static Position CurrentPosition = Position.Stowed;
    public static Position TargetPosition = Position.Stowed;
    public static Status CurrentStatus = Status.Waiting;
//#endregion



//#region Internal Controls
    @Override
    public void periodic() {
        // TODO: Detect when pieces enter into the hopper (roughly), and interface with the hopper's FuelCount
    }
//#endregion



//#region External Controls
    // TODO: Stow the intake using internal controls
    public static Command Stow() {
        // Stop rollers, move up
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    } 
    // TODO: Engage the intake using internal controls
    public static Command Engage() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Engage the extake using internal controls
    public static Command EngageExtake() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion
}
