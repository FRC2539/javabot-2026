package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.shooter.ShooterIO.ShooterIOInputs;

public class ShooterSubsystem extends SubsystemBase {

    private ShooterIO shooterIO;
    public ShooterIOInputsAutoLogged shooterInputs = new ShooterIOInputsAutoLogged();

    private ShooterStatus currentStatus = ShooterStatus.Ready;

    public static enum ShooterStatus {
        Accelerating,
        Ready
    }

    public ShooterStatus getStatus() {
        return currentStatus;
    }
    
    public ShooterSubsystem(ShooterIO io) {
        shooterIO = io;

        setDefaultCommand(Commands.run(() -> setTargetRPM(ShooterConstants.IdleRPS)));
    }

    @Override
    public void periodic() {
        shooterIO.updateInputs(shooterInputs);
        Logger.processInputs("RealOutputs/ShooterSubsystem", shooterInputs);

        if (shooterIO.isAtSetpoint()) {
            currentStatus = ShooterStatus.Ready;
        } else {
            currentStatus = ShooterStatus.Accelerating;
        }
    }

    public void setTargetRPM(double targetRPS) {
        shooterIO.setControlVelocity(targetRPS);
    }








}
