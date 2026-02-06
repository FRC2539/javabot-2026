package frc.robot.subsystems.Kirkulator;

import frc.robot.util.LoggedTunableNumber;
import frc.robot.util.Subsystemutil;
import org.littletonrobotics.junction.AutoLogOutput;

public class KirkulatorSubsystem extends Subsystemutil {

  private static final LoggedTunableNumber kirkulatorShootVolts =
      new LoggedTunableNumber("Hopper/ShootVolts", 12.0);
  private static final LoggedTunableNumber kirkulatorReverseVolts =
      new LoggedTunableNumber("Hopper/ReverseVolts", -12.0);
  private static final LoggedTunableNumber kirkulatorAdjustVolts =
      new LoggedTunableNumber("Hopper/AdjustVolts", 0.0);

  private KirkulatorIO kirkulatorIO;

  @AutoLogOutput private Goal goal = Goal.STOP;

  public KirkulatorSubsystem(KirkulatorIO kirkulatorIO) {
    this.kirkulatorIO = kirkulatorIO;
  }

  public void periodic() {

    double kirkulatorVoltage = 0.0;

    switch (goal) {
      case SHOOT -> {
        kirkulatorVoltage = kirkulatorShootVolts.get();
      }
      case REVERSE -> {
        kirkulatorVoltage = kirkulatorReverseVolts.get();
      }
      case STOP -> {
        kirkulatorVoltage = 0.0;
      }
      case ADJUST -> {
        kirkulatorVoltage = kirkulatorAdjustVolts.get();
      }
    }
    kirkulatorIO.setVoltage(kirkulatorVoltage);
  }

  @Override
  public void periodicAfterScheduler() {}

  public enum Goal {
    SHOOT,
    REVERSE,
    STOP,
    ADJUST
  }
}
