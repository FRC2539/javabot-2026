package frc.robot.subsystems.hopper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HopperSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public HopperSubsystem(int initialFuel) {
        FuelCount = initialFuel;
    }

//#region Internal Data
//#endregion


//#region Shared Data
    public static int FuelCount;
//#endregion


    

//#region Internal Controls
//#endregion


//#region External Controls
//#endregion
}
