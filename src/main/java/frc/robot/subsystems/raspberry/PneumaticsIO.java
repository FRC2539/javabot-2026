package frc.robot.subsystems.raspberry;

import org.littletonrobotics.junction.AutoLog;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public interface PneumaticsIO {

  void updateInputs(PneumaticsIOInputs inputs);

  void setIntakeSolenoid(Value value);
  void setRaspberrySolenoid(Value value);
  void setRaspberry2Solenoid(Value value);

  @AutoLog
  class PneumaticsIOInputs {
    public Value intakeState = Value.kOff;
    public Value raspberry = Value.kOff; //v
    public Value raspberry2 = Value.kOff;
  }
}
