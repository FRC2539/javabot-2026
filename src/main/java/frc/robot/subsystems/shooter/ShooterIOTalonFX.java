package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ShooterIOTalonFX implements ShooterIO {

    private TalonFX leftMotor = new TalonFX(ShooterConstants.leftShooterMotorID, ShooterConstants.shooterCanBus);
    private TalonFX rightMotor = new TalonFX(ShooterConstants.rightShooterMotorID, ShooterConstants.shooterCanBus);
    private final MotionMagicVelocityVoltage controlRequest = new MotionMagicVelocityVoltage(0);

    private double targetRPS = 0;

    public ShooterIOTalonFX() {
        leftMotor.setNeutralMode(NeutralModeValue.Coast);
        rightMotor.setNeutralMode(NeutralModeValue.Coast);

        leftMotor.getConfigurator().apply(ShooterConstants.leftMotorConfig);
        rightMotor.getConfigurator().apply(ShooterConstants.rightMotorConfig);

        rightMotor.setControl(new Follower(leftMotor.getDeviceID(), MotorAlignmentValue.Opposed));
    }

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.wheelVelocity = leftMotor.getVelocity().getValueAsDouble();
        inputs.leftMotorTemperatureCelcius = leftMotor.getDeviceTemp().getValueAsDouble();
        inputs.rightMotorTemperatureCelcius = rightMotor.getDeviceTemp().getValueAsDouble();
    }

    @Override
    public void setControlVelocity(double targetVelocity) {
        this.targetRPS = targetVelocity;
        leftMotor.setControl(controlRequest.withVelocity(targetVelocity));
    }

    @Override
    public boolean isAtSetpoint() {
        double currentSpeed = leftMotor.getVelocity().getValueAsDouble();
        double error = Math.abs(targetRPS - currentSpeed);

        if (error < ShooterConstants.goalDeadbandRPS) {
            return true;
        } else {
            return false;
        }
    } 
}
