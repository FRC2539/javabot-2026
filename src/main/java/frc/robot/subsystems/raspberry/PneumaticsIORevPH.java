package frc.robot.subsystems.raspberry;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.constants.PneumaticsConstants;

public class PneumaticsIORevPH implements PneumaticsIO {

  private final DoubleSolenoid intakeSolenoid =
      new DoubleSolenoid(
          PneumaticsModuleType.REVPH,
          PneumaticsConstants.intakeForward,
          PneumaticsConstants.intakeReverse);

  private final DoubleSolenoid raspberrySolenoid =
      new DoubleSolenoid(
          PneumaticsModuleType.REVPH,
          PneumaticsConstants.raspberryForward,
          PneumaticsConstants.raspberryReverse);

  private final DoubleSolenoid raspberry2Solenoid =
      new DoubleSolenoid(
          PneumaticsModuleType.REVPH,
          PneumaticsConstants.raspberry2Forward,
          PneumaticsConstants.raspberry2Reverse);

  @Override
  public void updateInputs(PneumaticsIOInputs inputs) {
    inputs.intakeState = intakeSolenoid.get();
    inputs.raspberry = raspberrySolenoid.get();
    inputs.raspberry2 = raspberry2Solenoid.get();
  }

  @Override
  public void setIntakeSolenoid(Value value) {
    intakeSolenoid.set(value);
  }

  @Override
  public void setRaspberrySolenoid(Value value) {
    raspberrySolenoid.set(value);
  }

  @Override
  public void setRaspberry2Solenoid(Value value) {
    raspberry2Solenoid.set(value);
  }
}
