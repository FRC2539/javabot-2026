package frc.robot.subsystems.climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public ClimberSubsystem() {
        // TODO: Initialize "height" to read climber absolute position 
    }

//#region Internal Data 
    private static DoubleSupplier height = (() -> { if (AllowUnimplementedErrors) System.err.println("Supplier not initialized"); return 0; });
//#endregion


//#region Shared Data
    // TODO: Encode climber heights into enum
    enum Stage {
        INVALID,

        L0,
        L1,
        L2,
        L3
    }
    
    public static double Height() { return height.getAsDouble(); } 

    public static Stage CurrentStage = Stage.INVALID;
    public static Stage TargetStage = Stage.INVALID;
//#endregion


    

//#region Internal Controls
    // TODO: Assemble a command that travels from the current stage to the new target stage
    private static Command setTargetLevel(Stage mode) {
        // Sets TargetStage to L1 on completion. Utilizes L|A|_L|B|() functions
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }

    // TODO: Climb from L0 to L1
    private static Command L0_L1() {
        // Sets CurrentStage to L1 on completion
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Climb from L1 to L0
    private static Command L1_L0() {
        // Sets CurrentStage to L0 on completion
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Climb from L1 to L2
    private static Command L1_L2() {
        // Sets CurrentStage to L2 on completion
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Climb from L2 to L3
    private static Command L2_L3() {
        // Sets CurrentStage to L3 on completion
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion


//#region External Controls
    // TODO: Set the climbing target to level 1
    public static Command ClimbL1() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Set the climbing target to level 2
    public static Command ClimbL2() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Set the climbing target to level 3
    public static Command ClimbL3() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
    // TODO: Climb down drom level 1, used at the end of auto/start of teleop
    public static Command ClimbDown() {
        return Commands.runOnce(() -> { if (AllowUnimplementedErrors) System.err.println("Command not implemented"); });
    }
//#endregion
}
