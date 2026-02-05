package frc.robot.subsystems.hopper;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HopperIORev implements HopperIO {

  private final DoubleSolenoid intakeSolenoid =
      new DoubleSolenoid(
          HopperConstants.moduleType,
          HopperConstants.intakeChannelForward,
          HopperConstants.intakeChannelReverse);

  private final DoubleSolenoid solenoid1 =
      new DoubleSolenoid(
          HopperConstants.moduleType,
          HopperConstants.solenoid1ChannelForward,
          HopperConstants.Solenoid1ChannelReverse);

  private final DoubleSolenoid solenoid2 =
      new DoubleSolenoid(
          HopperConstants.moduleType,
          HopperConstants.solenoid2ChannelForward,
          HopperConstants.solenoid2ChannelReverse);

  @Override
  public void updateInputs(HopperIOInputs inputs) {
    inputs.isIntakeExtended = intakeSolenoid.get().equals(Value.kForward);
    inputs.isSolenoid1Extended = intakeSolenoid.get().equals(Value.kForward);
    inputs.isSolenoid2Extended = intakeSolenoid.get().equals(Value.kForward);
  }

  @Override
  public void setIntakeSolenoidState(Value desiredState) {
    intakeSolenoid.set(desiredState);
  }

  @Override
  public void setSolenoid1State(Value desiredState) {
    solenoid1.set(desiredState);
  }

  @Override
  public void setSolenoid2State(Value desiredState) {
    solenoid2.set(desiredState);
  }
}
