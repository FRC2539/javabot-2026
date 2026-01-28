package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
    public void updateInputs(ShooterIOInputs inputs);

    @AutoLog
    public class ShooterIOInputs {
        public double wheelVelocity = 0; // RPM
        public double leftMotorTemperatureCelcius = 0;
        public double rightMotorTemperatureCelcius = 0;
    }

    public boolean isAtSetpoint();

    public void setControlVelocity(double targetVelocity);

}