package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private ShooterIO shooterIO;
    public ShooterIOInputsAutoLogged shooterInputs = new ShooterIOInputsAutoLogged();

    public ShooterSubsystem(ShooterIO io) {
        shooterIO = io;

        setDefaultCommand(Commands.run(() -> setTargetRPM(ShooterConstants.IdleRPS)));
    }

    @Override
    public void periodic() {
        shooterIO.updateInputs(shooterInputs);
        Logger.processInputs("RealOutputs/ShooterSubsystem", shooterInputs);

    }

    public void setTargetRPM(double targetRPS) {
        shooterIO.setControlVelocity(targetRPS);
    }
}
