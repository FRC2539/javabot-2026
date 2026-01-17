package frc.robot.subsystems.lights;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.hardware.CANdle;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightsSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public LightsSubsystem() {
        if (!RobotBase.isReal()) return;
        candle = new CANdle(CANDLE_PORT, "CANivore");
    }

//#region Internal Data
    // Hardware
    private static final int CANDLE_PORT = 18;
    private static CANdle candle;

    // Timers
    private static final Timer RobotStatusTimer = new Timer();
    private static final Timer AlertTimer = new Timer();
//#endregion


//#region Shared Data
    public static boolean IsOn = true;
    public static double GlobalBrightness = 1;

    public static double TimeSinceStatusChange() { return RobotStatusTimer.get(); }
    public static double TimeSinceAlert() { return AlertTimer.get(); }
//#endregion


    

//#region Internal Controls
    // TODO: Lights internal controls
//#endregion


//#region External Controls
    // TODO: Turn lights on/off
    public static void TurnOn() {

    }
    public static void TurnOff() {
        
    }
    // TODO: Set lights brightness
    public static void SetGlobalBrightness(double scale) {

    }
//#endregion
}
