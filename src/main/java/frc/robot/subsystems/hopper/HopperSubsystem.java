package frc.robot.subsystems.hopper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HopperSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public HopperSubsystem() {
        // TODO: Find out how many fuel are initially in the robot? Only considered when preloading/restarting the robot, though
        FuelCount = 0;
    }

//#region Internal Data
//#endregion


//#region Shared Data
    public static int FuelCount;
//#endregion


    

//#region Internal Controls
//#endregion


//#region External Controls
    // TODO: Interface with the hopper counter
    // TODO: (Externally) Add an manual method for adding/removing fuel
    public static void AddFuel() {
        if (AllowUnimplementedErrors) System.err.println("Function not implemented");
    }
    public static void RemoveFuel() {
        if (AllowUnimplementedErrors) System.err.println("Function not implemented");
    }
    public static void SetFuel(int count) {
        if (AllowUnimplementedErrors) System.err.println("Function not implemented");
    }
//#endregion
}
