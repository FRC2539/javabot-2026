package frc.robot.subsystems.input;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.controller.LogitechController;
import frc.robot.lib.controller.ThrustmasterJoystick;

public class InputSubsystem extends SubsystemBase {
  public InputSubsystem() {
    MatchTimer.start();
  }

  // #region Data Types
  public static enum MatchPeriod {
    Disabled,
    Auto,
    Teleop,
    Test,
    EStop
  }

  public static enum MatchTimeframe {
    None(0),
    Auto(20),
    TransitionShift(10),
    Shift1(25),
    Shift2(25),
    Shift3(25),
    Shift4(25),
    EndGame(30);

    public double duration;

    private MatchTimeframe(double duration) {
      this.duration = duration;
    }
  }

  public static enum HubActivity {
    Both,
    Ally,
    Opponent
  }

  // #region Periodic
  @Override
  public void periodic() {
    UpdateMatchPeriod();
    UpdateMatchTimeframe();
  }

  // #region Controllers
  // Driver
  public static final ThrustmasterJoystick driverLeftJoystick = new ThrustmasterJoystick(0);
  public static final ThrustmasterJoystick driverRightJoystick = new ThrustmasterJoystick(1);

  // Operator
  public static final LogitechController operatorController = new LogitechController(2);

  // #region Game Data

  static Timer MatchTimer = new Timer();
  static Timer MatchPeriodTimer = new Timer();
  static Timer MatchTimeframeTimer = new Timer();
  public static Alliance alliance;

  public static MatchPeriod getMatchPeriod() {
    if (DriverStation.isEStopped()) return MatchPeriod.EStop;
    if (DriverStation.isDisabled()) return MatchPeriod.Disabled;
    if (DriverStation.isAutonomous()) return MatchPeriod.Auto;
    if (DriverStation.isTeleop()) return MatchPeriod.Teleop;
    if (DriverStation.isTest()) return MatchPeriod.Test;
    // Fallback
    return MatchPeriod.Disabled;
  }

  static MatchTimeframe getMatchTimeframe() {
    switch (currentMatchTimeframe) {
      case None:
        if (currentMatchPeriod == MatchPeriod.Auto) return MatchTimeframe.Auto;
        if (currentMatchPeriod == MatchPeriod.Teleop) return MatchTimeframe.TransitionShift;
        break;
      case Auto:
        if (currentMatchPeriod == MatchPeriod.Auto) break;
        if (currentMatchPeriod == MatchPeriod.Teleop) return MatchTimeframe.TransitionShift;
        return MatchTimeframe.None;
      case TransitionShift:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchPeriodTimer.get() < MatchTimeframe.TransitionShift.duration) break;
        return MatchTimeframe.Shift1;
      case Shift1:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchPeriodTimer.get() < MatchTimeframe.Shift1.duration) break;
        return MatchTimeframe.Shift2;
      case Shift2:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchPeriodTimer.get() < MatchTimeframe.Shift2.duration) break;
        return MatchTimeframe.Shift3;
      case Shift3:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchPeriodTimer.get() < MatchTimeframe.Shift3.duration) break;
        return MatchTimeframe.Shift4;
      case Shift4:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchPeriodTimer.get() < MatchTimeframe.Shift4.duration) break;
        return MatchTimeframe.EndGame;
      case EndGame:
        if (currentMatchPeriod == MatchPeriod.Teleop) break;
        break;
    }

    return currentMatchTimeframe;
  }

  public static boolean IsHubActive() {
    return GetHubActivity() != HubActivity.Opponent;
  }

  public static HubActivity GetHubActivity() {
    return switch (currentMatchTimeframe) {
      case None, Auto, TransitionShift -> HubActivity.Both;
      case Shift1, Shift3 -> AutoLoser();
      case Shift2, Shift4 -> OppositeActivity(AutoLoser());
      default -> HubActivity.Both;
    };
  }

  public static HubActivity AutoLoser() {
    String s = DriverStation.getGameSpecificMessage();
    if (s.isEmpty() || DriverStation.getAlliance().isEmpty()) return HubActivity.Both;
    if (s.substring(0, 1).equals("R"))
      return (DriverStation.getAlliance().get().equals(Alliance.Red))
          ? HubActivity.Ally
          : HubActivity.Opponent;
    if (s.substring(0, 1).equals("B"))
      return (DriverStation.getAlliance().get().equals(Alliance.Blue))
          ? HubActivity.Ally
          : HubActivity.Opponent;
    return HubActivity.Both;
  }

  public static HubActivity OppositeActivity(HubActivity hubActivity) {
    return switch (hubActivity) {
      case Both -> HubActivity.Both;
      case Ally -> HubActivity.Opponent;
      case Opponent -> HubActivity.Ally;
      default -> HubActivity.Both;
    };
  }

  // #region Match Period/Timeframe Managment
  public static MatchPeriod currentMatchPeriod = MatchPeriod.Disabled;

  static void UpdateMatchPeriod() {
    MatchPeriod newMatchPeriod = getMatchPeriod();
    if (currentMatchPeriod == newMatchPeriod) return;

    // When switching from...
    switch (currentMatchPeriod) {
      case Disabled:
        MatchTimer.restart();
        break;
      case Auto:
        break;
      case Teleop:
        break;
      case Test:
        break;
      case EStop:
        break;
    }

    // When switching to...
    switch (newMatchPeriod) {
      case Disabled:
        MatchTimer.stop();
        break;
      case Auto:
        break;
      case Teleop:
        break;
      case Test:
        break;
      case EStop:
        break;
    }

    currentMatchPeriod = newMatchPeriod;
    MatchPeriodTimer.reset();
  }

  public static MatchTimeframe currentMatchTimeframe = MatchTimeframe.None;

  static void UpdateMatchTimeframe() {
    MatchTimeframe newMatchTimeframe = getMatchTimeframe();
    if (currentMatchTimeframe == newMatchTimeframe) return;

    // When switching from...
    switch (currentMatchTimeframe) {
      case None:
        break;
      case Auto:
        break;
      case TransitionShift:
        break;
      case Shift1:
        break;
      case Shift2:
        break;
      case Shift3:
        break;
      case Shift4:
        break;
      case EndGame:
        break;
    }

    // When switching to...
    switch (newMatchTimeframe) {
      case None:
        break;
      case Auto:
        break;
      case TransitionShift:
        break;
      case Shift1:
        break;
      case Shift2:
        break;
      case Shift3:
        break;
      case Shift4:
        break;
      case EndGame:
        break;
    }

    MatchTimeframeTimer.advanceIfElapsed(currentMatchTimeframe.duration);
    currentMatchTimeframe = newMatchTimeframe;
  }
}
