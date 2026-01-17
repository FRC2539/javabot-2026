package frc.robot.subsystems.controls;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;
import frc.robot.subsystems.turret.TurretSubsystem;

public class ControlsSubsystem extends SubsystemBase {
    static final boolean AllowUnimplementedErrors = false;
    public ControlsSubsystem() {
        bindButtons();
    }

//#region Internal Data
//#endregion


//#region Shared Data
    // Driver
    public static final ThrustmasterJoystick driverLeftJoystick = new ThrustmasterJoystick(0);
    public static final ThrustmasterJoystick driverRightJoystick = new ThrustmasterJoystick(1);
    
    // Operator
    public static final LogitechController operatorController = new LogitechController(2);
//#endregion


    

//#region Internal Controls
    public void bindButtons() {
        // TODO: Driver/operator control schemes
        
        // Examples:
        operatorController.getLeftTrigger().and(operatorController.getRightTrigger().negate()).whileTrue(TurretSubsystem.Prepare());
        operatorController.getRightTrigger().whileTrue(TurretSubsystem.Shoot());
    }
//#endregion


//#region External Controls
//#endregion
}
