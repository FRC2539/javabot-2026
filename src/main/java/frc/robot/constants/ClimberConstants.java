package frc.robot.constants;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

public final class ClimberConstants {

    

    //#region Motor IDs and CAN Bus
    public static final int leftMotorId = 10;  // Replace with actual CAN ID
    public static final int rightMotorId = 11; // Replace with actual CAN ID
    public static final String canBus = "CANivore";
    //#endregion

    //#region Current limits
    public static final int currentLimit = 40;
    //#endregion

    //#region Position limits 
    public static final double lowerLimitRotations = 0.0;
    public static final double upperLimitRotations = 20.0; // adjust to actual max
    public static final double positionTolerance = 0.05; // rotations tolerance for setpoint
    //#endregion
}
