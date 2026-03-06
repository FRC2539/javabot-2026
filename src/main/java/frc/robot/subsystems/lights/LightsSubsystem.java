package frc.robot.subsystems.lights;

import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StatusLedWhenActiveValue;
import com.ctre.phoenix6.signals.StripTypeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.input.InputSubsystem;
import frc.robot.subsystems.input.InputSubsystem.HubActivity;
import frc.robot.subsystems.input.InputSubsystem.MatchPeriod;
import frc.robot.subsystems.input.InputSubsystem.MatchTimeframe;
import frc.robot.subsystems.lights.LightsConstants.ColorPalette;
import frc.robot.subsystems.shooter.flywheel.FlywheelIO;
import frc.robot.subsystems.shooter.hood.HoodIO;
import frc.robot.subsystems.shooter.turret.TurretIO;
import java.util.function.BooleanSupplier;

public class LightsSubsystem extends SubsystemBase {

  public static CANdle candle = new CANdle(LightsConstants.CandleID);

  public LightsSubsystem() {
    CANdleConfiguration cfg = new CANdleConfiguration();
    cfg.CANdleFeatures.StatusLedWhenActive = StatusLedWhenActiveValue.Disabled;
    cfg.LED.StripType = StripTypeValue.GRB;
    cfg.LED.BrightnessScalar = 1.0;
    candle.getConfigurator().apply(cfg);

    LEDSegment.ClearAnimIDs(0, 7);
    // Presets.RainbowSplit(false);
  }

  public enum AnimationState {
    DISABLED,
    PLAYING,
    INTAKING,
    SHOOTING,
    CLIMBING,

    IDLE,
    ESTOP,
    ERROR,
  }

  // #region Periodic
  @Override
  public void periodic() {
    // setSolidColor(ColorPalette.Red);
    // setRGBFadeAnimation(5);
    // setSolidColor(ColorPalette.Red);
    // Presets.FlowQuads(ColorPalette.Orange, 3, false);

    switch (getAnimationState()) {
      case DISABLED:
        if (gameEnded) Presets.RainbowSplit(false);
        else Presets.Fire(false);
        break;
      case PLAYING, SHOOTING:
        if (InputSubsystem.currentMatchTimeframe.duration - InputSubsystem.MatchTimeframeTimer.get()
            <= 5) {
          if (InputSubsystem.currentMatchTimeframe == MatchTimeframe.EndGame) {
            Presets.Strobe(ColorPalette.Red, 0.25);
            gameEnded = true;
          } else {
            gameEnded = false;
            if (InputSubsystem.getWillActivitySwap())
              Presets.Strobe(
                  (InputSubsystem.IsHubActive()) ? ColorPalette.Orange : ColorPalette.Yellow, 0.3);
          }
        } else {
          if (InputSubsystem.currentMatchTimeframe == MatchTimeframe.TransitionShift)
            Presets.FlowIndividualMerging(
                (InputSubsystem.AutoLoser() == HubActivity.Ally)
                    ? ColorPalette.White
                    : ColorPalette.Green,
                0.3,
                false);
          else
            Presets.SolidColor(
                (InputSubsystem.IsHubActive()) ? ColorPalette.Orange : ColorPalette.Yellow);
          gameEnded = false;
        }
        break;
      case INTAKING:
        Presets.Fade(ColorPalette.Blue, 0.5);
        break;
        // case SHOOTING:
        //   double ready = 1;
        //   // if (turretIO.getExpectedDelta() > 0.01)
        //   //   ready -= Math.abs(turretIO.getExpectedDelta()) * 50; // TODO: Tweak Constants
        //   // if (hoodIO.getExpectedDelta() > 0.01) ready -= Math.abs(hoodIO.getExpectedDelta()) *
        // 50;
        //   // if (flywheelIO.getExpectedDelta() > 1) ready -=
        // Math.abs(flywheelIO.getExpectedDelta()) / 5;
        //   if (ready < 0) ready = 0;

        //   if (ready >= 0.995) Presets.SolidColor(ColorPalette.Green);
        //   else Presets.SolidColor(ColorPalette.Crossfade(ColorPalette.Red, ColorPalette.Yellow,
        // ready));
        //   break;
      case CLIMBING:
        Presets.Strobe(ColorPalette.Green, 0.25);
        break;
      case IDLE:
        Presets.Fade(ColorPalette.White, 10);
        break;
      case ESTOP:
        Presets.Strobe(ColorPalette.Red, 0.5);
        break;
      case ERROR:
        Presets.SolidColor(ColorPalette.Yellow);
        break;
    }
  }

  // #region Animation States
  public boolean gameEnded = false;
  public BooleanSupplier isAiming = () -> false;
  public BooleanSupplier isShooting = () -> false;
  public BooleanSupplier isClimbing = () -> false;
  public BooleanSupplier isIntaking = () -> false;

  public static TurretIO turretIO;
  public static HoodIO hoodIO;
  public static FlywheelIO flywheelIO;

  AnimationState getAnimationState() {
    MatchPeriod period = InputSubsystem.getMatchPeriod();

    switch (period) {
      case Disabled:
        if (InputSubsystem.MatchPeriodTimer.get() > 180)
          return AnimationState.IDLE; // After 3 minutes
        return AnimationState.DISABLED;
      case Teleop, Auto, Test:
        if (isShooting.getAsBoolean() && !isAiming.getAsBoolean()) return AnimationState.ERROR;
        if (isAiming.getAsBoolean()) return AnimationState.SHOOTING;
        if (isClimbing.getAsBoolean()) return AnimationState.CLIMBING;
        if (isIntaking.getAsBoolean()) return AnimationState.INTAKING;
        return AnimationState.PLAYING;
      case EStop:
        return AnimationState.ESTOP;
    }
    return AnimationState.ERROR;
  }

  // #region Animation Presets
  static class Presets {
    static Mode lastMode = Mode.Off;
    static RGBWColor lastColor = null;
    static double lastPeriod = 0;
    static boolean lastInverted = false;

    // Uniform
    public static void SolidColor(RGBWColor color) {
      if (lastMode == Mode.SolidColor && color == lastColor) return;
      lastMode = Mode.SolidColor;
      lastColor = color;
      LEDSegment.ClearAnimIDs(0, 7);
      LEDSegment.All.setSolidColor(color);
    }

    public static void Strobe(RGBWColor color, double period) {
      if (lastMode == Mode.Strobe && lastColor == color && lastPeriod == period) return;
      lastMode = Mode.Strobe;
      lastColor = color;
      lastPeriod = period;
      LEDSegment.ClearAnimIDs(1, 7);
      LEDSegment.All.setStrobeAnimation(color, period);
    }

    public static void Fade(RGBWColor color, double period) {
      if (lastMode == Mode.Fade && lastColor == color && period == lastPeriod) return;
      lastMode = Mode.Fade;
      lastColor = color;
      lastPeriod = period;
      LEDSegment.ClearAnimIDs(1, 7);
      LEDSegment.All.setFadeAnimation(color, period);
    }

    public static void RGBFade(double period) {
      if (lastMode == Mode.RGBFade && lastPeriod == period) return;
      lastMode = Mode.RGBFade;
      lastPeriod = period;
      LEDSegment.ClearAnimIDs(1, 7);
      LEDSegment.All.setRGBFadeAnimation(period);
    }

    // Non-Uniform
    public static void Fire(boolean inverted) { // Flow to center (edges if inverted)
      if (lastMode == Mode.Fire && inverted == lastInverted) return;
      lastMode = Mode.Fire;
      lastInverted = inverted;
      for (LEDSegment s : LightsConstants.OuterSegments) s.setFireAnimation(0.4, 0.4, inverted, 40);
      for (LEDSegment s : LightsConstants.InnerSegments)
        s.setFireAnimation(0.4, 0.4, !inverted, 40);
    }

    public static void RainbowSingle(boolean inverted) { // Flow to left (right if inverted)
      if (lastMode == Mode.RainbowSingle && inverted == lastInverted) return;
      lastMode = Mode.RainbowSingle;
      lastInverted = inverted;
      for (LEDSegment s : LightsConstants.QuadsRight) s.setRainbowAnimation(1, inverted);
      for (LEDSegment s : LightsConstants.QuadsLeft) s.setRainbowAnimation(1, !inverted);
    }

    public static void RainbowSplit(boolean inverted) { // Flow to center (edges if inverted)
      if (lastMode == Mode.RainbowSplit && inverted == lastInverted) return;
      lastMode = Mode.RainbowSplit;
      lastInverted = inverted;
      for (LEDSegment s : LightsConstants.AllQuadSegments) s.setRainbowAnimation(1, inverted);
    }

    public static void FlowIndividualMerging(
        RGBWColor color,
        double period,
        boolean inverted) { // Flow to quad centers (quad edges if inverted)
      if (lastMode == Mode.FlowIndividualMerging
          && color.equals(lastColor)
          && period == lastPeriod
          && inverted == lastInverted) return;
      lastMode = Mode.FlowIndividualMerging;
      lastColor = color;
      lastPeriod = period;
      lastInverted = inverted;
      for (LEDSegment s : LightsConstants.OuterSegments)
        s.setFlowAnimation(color, period, inverted);
      for (LEDSegment s : LightsConstants.InnerSegments)
        s.setFlowAnimation(color, period, !inverted);
    }

    public static void FlowIndividualSynced(
        RGBWColor color, double period, boolean inverted) { // Flow to center (edges if inverted)
      if (lastMode == Mode.FlowIndividualSynced
          && color.equals(lastColor)
          && period == lastPeriod
          && inverted == lastInverted) return;
      lastMode = Mode.FlowIndividualSynced;
      lastColor = color;
      lastPeriod = period;
      lastInverted = inverted;
      for (LEDSegment s : LightsConstants.OuterSegments)
        s.setFlowAnimation(color, period, inverted);
    }

    public static void FlowQuads(
        RGBWColor color, double period, boolean inverted) { // Flow to center (edges if inverted)
      if (lastMode == Mode.FlowQuads
          && color.equals(lastColor)
          && period == lastPeriod
          && inverted == lastInverted) return;
      lastMode = Mode.FlowQuads;
      lastColor = color;
      lastPeriod = period;
      lastInverted = inverted;
      for (LEDSegment s : LightsConstants.AllQuadSegments)
        s.setFlowAnimation(color, period, inverted);
    }

    // // Shooter Interrupt
    // static boolean shooterInterrupted = false;
    // public static void releaseShooter() { shooterInterrupted = false; }
    // public static void interruptShooter() {
    //   shooterInterrupted = true;
    //   for (LEDSegment s : LightsConstants.ShooterSegments)
    // }

    enum Mode {
      Off,
      SolidColor,
      Strobe,
      Fade,
      RGBFade,
      Fire,
      RainbowSingle,
      RainbowSplit,
      FlowIndividualMerging,
      FlowIndividualSynced,
      FlowQuads
    }
  }

  // #region Segmentation

  public void setBrightness(double brightness) {
    if (candle != null) {
      var cfg = new CANdleConfiguration();
      cfg.LED.BrightnessScalar = brightness;
      candle.getConfigurator().apply(cfg.LED);
    }
  }

  public void disableLEDs() {
    setBrightness(0.0);
  }

  public void enableLEDs() {
    setBrightness(1.0);
  }
}
