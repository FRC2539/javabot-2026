package frc.robot.subsystems.hood;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;
import frc.robot.constants.HoodConstants;
import static frc.robot.constants.HoodConstants.kGearRatio;


public class HoodIOTalonFX implements HoodIO {

  private final TalonFX motor = new TalonFX(HoodConstants.kMotorId, HoodConstants.kCanBus);
  private final MotionMagicVoltage mmRequest = new MotionMagicVoltage(0.0);

  public HoodIOTalonFX() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    motor.getConfigurator().apply(config);
  }

  public void configurePID(double kP, double kD) {
    Slot0Configs slot0 = new Slot0Configs();
    slot0.kP = kP;
    slot0.kD = kD;
    motor.getConfigurator().apply(slot0);
  }

  public void configureMotionMagic(double cruiseRadPerSec, double accelRadPerSecSq) {
    MotionMagicConfigs mm = new MotionMagicConfigs();
    mm.MotionMagicCruiseVelocity =
        Units.radiansToRotations(cruiseRadPerSec * kGearRatio);
    mm.MotionMagicAcceleration =
        Units.radiansToRotations(accelRadPerSecSq * kGearRatio);
    motor.getConfigurator().apply(mm);
  }

  @Override
  public void updateInputs(HoodIOInputs inputs) {
    inputs.motorConnected = motor.isConnected();

    inputs.positionRad =
        Units.rotationsToRadians(motor.getPosition().getValueAsDouble()) / kGearRatio;
    inputs.velocityRadPerSec =
        Units.rotationsToRadians(motor.getVelocity().getValueAsDouble()) / kGearRatio;

    inputs.appliedVolts = motor.getMotorVoltage().getValueAsDouble();
    inputs.supplyCurrentAmps = motor.getSupplyCurrent().getValueAsDouble();
    inputs.torqueCurrentAmps = motor.getTorqueCurrent().getValueAsDouble();
    inputs.tempCelsius = motor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setBrakeMode(boolean brake) {
    motor.setNeutralMode(brake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
  }

  @Override
  public void setMotionMagic(double positionRad) {
    mmRequest.Position =
        Units.radiansToRotations(positionRad * kGearRatio);
    motor.setControl(mmRequest);
  }

  @Override
  public void stop() {
    motor.stopMotor();
  }
}