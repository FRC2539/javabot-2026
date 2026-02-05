package frc.robot.subsystems.hopper;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import org.littletonrobotics.junction.AutoLog;

public interface HopperIO {

  void updateInputs(HopperIOInputs inputs);

  void setIntakeSolenoidState(Value desiredState);

  void setSolenoid1State(Value desiredState);

  void setSolenoid2State(Value desiredState);

  @AutoLog
  class HopperIOInputs {
    boolean isIntakeExtended = false;
    boolean isSolenoid1Extended = false;
    boolean isSolenoid2Extended = false;
  }
}
