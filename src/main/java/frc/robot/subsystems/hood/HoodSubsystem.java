package frc.robot.subsystems.hood;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;

import org.littletonrobotics.junction.Logger;

public class HoodSubsystem extends SubsystemBase {

  private final HoodIO io;
  private final HoodIOInputsAutoLogged inputs =
    new HoodIOInputsAutoLogged();


  private static final LoggedTunableNumber kP =
      new LoggedTunableNumber("Hood/kP", 30000.0);
  private static final LoggedTunableNumber kD =
      new LoggedTunableNumber("Hood/kD", 300.0);

  private static final LoggedTunableNumber minAngleDeg =
      new LoggedTunableNumber("Hood/MinAngleDeg", 19.0);
  private static final LoggedTunableNumber maxAngleDeg =
      new LoggedTunableNumber("Hood/MaxAngleDeg", 51.0);

  private static final LoggedTunableNumber cruiseDegPerSec =
      new LoggedTunableNumber("Hood/MMCruiseDegPerSec", 600.0);
  private static final LoggedTunableNumber accelDegPerSecSq =
      new LoggedTunableNumber("Hood/MMAccelDegPerSecSq", 1200.0);

  private static final LoggedTunableNumber toleranceDeg =
      new LoggedTunableNumber("Hood/ToleranceDeg", 1.0);



  private double goalAngleRad = 0.0;
  private double goalVelocityRadPerSec = 0.0;

  private boolean zeroed = false;
  private double offsetRad = 0.0;

  public HoodSubsystem(HoodIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Hood", inputs);


    if (io instanceof HoodIOTalonFX talon) {
      talon.configurePID(kP.get(), kD.get());
      talon.configureMotionMagic(
          Units.degreesToRadians(cruiseDegPerSec.get()),
          Units.degreesToRadians(accelDegPerSecSq.get()));
    }

    if (DriverStation.isDisabled() || !zeroed) {
      io.setBrakeMode(true);
      io.stop();
      return;
    }

    double minRad = Units.degreesToRadians(minAngleDeg.get());
    double maxRad = Units.degreesToRadians(maxAngleDeg.get());

    double targetRad =
        MathUtil.clamp(goalAngleRad, minRad, maxRad) - offsetRad;

    io.setMotionMagic(targetRad);

    Logger.recordOutput("Hood/GoalAngleRad", goalAngleRad);
    Logger.recordOutput("Hood/GoalVelocityRadPerSec", goalVelocityRadPerSec);
  }

//   public void setFromShotCalculator() {
//     var params = ShotCalculator.getInstance().getParameters();
//     goalAngleRad = params.hoodAngle();
//     goalVelocityRadPerSec = params.hoodVelocity();
//   } 
//USE ABOVE WHEN SHOT CALCULATOR IS READY

  public void setManual(double angleRad, double velocityRadPerSec) {
    goalAngleRad = angleRad;
    goalVelocityRadPerSec = velocityRadPerSec;
  }

  public void zero() {
    offsetRad =
        Units.degreesToRadians(minAngleDeg.get()) - inputs.positionRad;
    zeroed = true;
  }

  public double getMeasuredAngleRad() {
    return inputs.positionRad + offsetRad;
  }

  public boolean atGoal() {
    return DriverStation.isEnabled()
        && zeroed
        && Math.abs(getMeasuredAngleRad() - goalAngleRad)
            <= Units.degreesToRadians(toleranceDeg.get());
  }
}
