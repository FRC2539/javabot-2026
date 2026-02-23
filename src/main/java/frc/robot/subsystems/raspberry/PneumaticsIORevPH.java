package frc.robot.subsystems.raspberry;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class PneumaticsIORevPH implements PneumaticsIO {

  private final PneumaticHub pneumaticHub = new PneumaticHub(PneumaticsConstants.pneumaticHubId);

  private final DoubleSolenoid intakeSolenoid =
      new DoubleSolenoid(
          PneumaticsModuleType.REVPH,
          PneumaticsConstants.intakeForward,
          PneumaticsConstants.intakeReverse);

  private final DoubleSolenoid raspberry2Solenoid =
      new DoubleSolenoid(
          PneumaticsModuleType.REVPH,
          PneumaticsConstants.raspberry2Forward,
          PneumaticsConstants.raspberry2Reverse);

  public PneumaticsIORevPH() {
    pneumaticHub.enableCompressorAnalog(
        PneumaticsConstants.minPressure, PneumaticsConstants.maxPressure);
  }

  @Override
  public void updateInputs(PneumaticsIOInputs inputs) {
    inputs.intakeState = intakeSolenoid.get();
    inputs.raspberry2 = raspberry2Solenoid.get();
    inputs.pressurePsi = pneumaticHub.getPressure(0);
  }

  @Override
  public void setIntakeSolenoid(Value value) {
    intakeSolenoid.set(value);
  }

  @Override
  public void setRaspberry2Solenoid(Value value) {
    raspberry2Solenoid.set(value);
  }
}
