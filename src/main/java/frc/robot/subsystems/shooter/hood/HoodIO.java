package frc.robot.subsystems.shooter.hood;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;

public interface HoodIO {

  @AutoLog
  class HoodIOInputs {
    public double positionRad = 0.0;
    public double voltage = 0.0;
  }

  public void updateInputs(HoodIOInputs inputs);

  public void setTargetAngle(Rotation2d targetAngle);

  public boolean isAtSetpoint();
}
