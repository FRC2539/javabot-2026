package frc.robot.subsystems.input;

import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;
import org.littletonrobotics.junction.networktables.LoggedNetworkString;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    None(0, "Disabled"),
    Auto(20, "Auto"),
    TransitionShift(10, "Transition Shift"),
    Shift1(25, "Shift 1"),
    Shift2(25, "Shift 2"),
    Shift3(25, "Shift 3"),
    Shift4(25, "Shift 4"),
    EndGame(30, "End Game");

    public final double duration;
    public final String displayName;

    private MatchTimeframe(double duration, String displayName) {
      this.duration = duration;
      this.displayName = displayName;
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
    UpdateLogged();
  }

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
        if (MatchTimeframeTimer.get() < MatchTimeframe.TransitionShift.duration) break;
        return MatchTimeframe.Shift1;
      case Shift1:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchTimeframeTimer.get() < MatchTimeframe.Shift1.duration) break;
        return MatchTimeframe.Shift2;
      case Shift2:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchTimeframeTimer.get() < MatchTimeframe.Shift2.duration) break;
        return MatchTimeframe.Shift3;
      case Shift3:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchTimeframeTimer.get() < MatchTimeframe.Shift3.duration) break;
        return MatchTimeframe.Shift4;
      case Shift4:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
        if (MatchTimeframeTimer.get() < MatchTimeframe.Shift4.duration) break;
        return MatchTimeframe.EndGame;
      case EndGame:
        if (currentMatchPeriod != MatchPeriod.Teleop) return MatchTimeframe.None;
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

  // #region Period/Timeframe

  public static MatchPeriod currentMatchPeriod = MatchPeriod.Disabled;
  static void UpdateMatchPeriod() {
    MatchPeriod newMatchPeriod = getMatchPeriod();
    if (currentMatchPeriod == newMatchPeriod) return;

    // When switching from...
    switch (currentMatchPeriod) {
      case Disabled:
        MatchTimer.restart();
        MatchTimeframeTimer.restart();
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
        MatchTimeframeTimer.stop();
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
  
  //#region Logged
  public static LoggedNetworkString LoggedMatchTimeframeName = new LoggedNetworkString("match-timeframe-name", "test");
  public static LoggedNetworkNumber LoggedMatchTimeframeTimer = new LoggedNetworkNumber("match-timeframe-timer", 0);
  public static LoggedNetworkNumber LoggedMatchTimeframeTimerRatio = new LoggedNetworkNumber("match-timeframe-timer-ratio", 0);
  public static LoggedNetworkBoolean LoggedHubActivity = new LoggedNetworkBoolean("hub-activity", true);
  public static void UpdateLogged() {
    MatchTimeframe newMatchTimeframe = getMatchTimeframe();
    LoggedMatchTimeframeName.set(newMatchTimeframe.displayName);
    LoggedMatchTimeframeTimer.set(Math.ceil(Math.max(0, newMatchTimeframe.duration - MatchTimeframeTimer.get())));
    LoggedMatchTimeframeTimerRatio.set(Math.max(0, newMatchTimeframe.duration - MatchTimeframeTimer.get()) / newMatchTimeframe.duration);
    
    boolean isGoingToSwap = (newMatchTimeframe.duration - MatchTimeframeTimer.get()) < 3 && ((newMatchTimeframe == MatchTimeframe.TransitionShift && AutoLoser() == HubActivity.Opponent) || newMatchTimeframe == MatchTimeframe.Shift1 || newMatchTimeframe == MatchTimeframe.Shift2 || newMatchTimeframe == MatchTimeframe.Shift3 || (newMatchTimeframe == MatchTimeframe.Shift4 && AutoLoser() == HubActivity.Opponent));
    switch (GetHubActivity()) {
        case Both, Ally -> LoggedHubActivity.set(isGoingToSwap ? (newMatchTimeframe.duration - MatchTimeframeTimer.get()) % 1 < 0.5 : true);
        case Opponent -> LoggedHubActivity.set(isGoingToSwap ? (newMatchTimeframe.duration - MatchTimeframeTimer.get()) % 1 > 0.5 : false);
    }
  }
}
