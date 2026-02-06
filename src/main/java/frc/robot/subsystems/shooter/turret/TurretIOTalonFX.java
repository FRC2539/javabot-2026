package frc.robot.subsystems.shooter.turret;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;
import frc.robot.constants.TurretConstants;

public class TurretIOTalonFX implements TurretIO {

  private final TalonFX motor =
      new TalonFX(TurretConstants.TurretMotorId, TurretConstants.CanBus);

  private final MotionMagicVoltage mmRequest = new MotionMagicVoltage(0.0);

  public TurretIOTalonFX() {
    TalonFXConfiguration config = new TalonFXConfiguration();

    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    Slot0Configs slot0 = new Slot0Configs();
    slot0.kP = 8.0;
    slot0.kD = 0.2;
    config.Slot0 = slot0;

    MotionMagicConfigs mm = new MotionMagicConfigs();
    mm.MotionMagicCruiseVelocity =
        Units.radiansToRotations(
            TurretConstants.MaxVelRadPerSec * TurretConstants.GearRatio);
    mm.MotionMagicAcceleration =
        Units.radiansToRotations(
            TurretConstants.MaxAccelRadPerSec2 * TurretConstants.GearRatio);

    config.MotionMagic = mm;

    motor.getConfigurator().apply(config);
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {
    inputs.motorConnected = motor.isConnected();

    inputs.positionRad =
        Units.rotationsToRadians(motor.getPosition().getValueAsDouble())
            / TurretConstants.GearRatio;

    inputs.velocityRadPerSec =
        Units.rotationsToRadians(motor.getVelocity().getValueAsDouble())
            / TurretConstants.GearRatio;

    inputs.appliedVolts = motor.getMotorVoltage().getValueAsDouble();
    inputs.supplyCurrentAmps = motor.getSupplyCurrent().getValueAsDouble();
    inputs.tempCelsius = motor.getDeviceTemp().getValueAsDouble();
  }

  @Override
  public void setPositionRad(double positionRad) {
    mmRequest.Position =
        Units.radiansToRotations(positionRad * TurretConstants.GearRatio);
    motor.setControl(mmRequest);
  }

  @Override
  public void setVoltage(double volts) {
    motor.setVoltage(volts);
  }

  @Override
  public void stop() {
    motor.stopMotor();
  }
}
