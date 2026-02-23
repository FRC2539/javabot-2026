package frc.robot.subsystems.raspberry;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import org.littletonrobotics.junction.AutoLog;

public interface PneumaticsIO {

  void updateInputs(PneumaticsIOInputs inputs);

  void setIntakeSolenoid(Value value);

  void setRaspberry2Solenoid(Value value);

  @AutoLog
  class PneumaticsIOInputs {
    public Value intakeState = Value.kReverse;
    public Value raspberry2 = Value.kReverse;
    public double pressurePsi = 0.0;
  }
}
