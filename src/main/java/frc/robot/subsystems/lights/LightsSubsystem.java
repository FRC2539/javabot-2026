package frc.robot.subsystems.lights;

import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
import com.ctre.phoenix6.controls.SingleFadeAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.AnimationDirectionValue;
import com.ctre.phoenix6.signals.LarsonBounceValue;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StatusLedWhenActiveValue;
import com.ctre.phoenix6.signals.StripTypeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.input.InputSubsystem;
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
    // return;

    switch (getAnimationState()) {
      case DISABLED:
        if (gameEnded) Presets.RainbowSplit(false);
        else Presets.FlowQuads(ColorPalette.Orange, 3, false);
        gameEnded = false;
      case PLAYING:
        if (InputSubsystem.currentMatchTimeframe.duration - InputSubsystem.MatchTimeframeTimer.get()
            <= 3)
          if (InputSubsystem.currentMatchTimeframe == MatchTimeframe.EndGame) {
            Presets.Fade(ColorPalette.Red, 0.5);
          } else if (InputSubsystem.getWillActivitySwap()) {
            Presets.Fade(
                (InputSubsystem.IsHubActive()) ? ColorPalette.Orange : ColorPalette.Yellow, 1);
          } else
            Presets.SolidColor(
                (InputSubsystem.IsHubActive()) ? ColorPalette.Orange : ColorPalette.Yellow);
        if (InputSubsystem.currentMatchTimeframe == MatchTimeframe.EndGame) gameEnded = true;
      case INTAKING:
        Presets.Fade(ColorPalette.Blue, 0.5);
      case SHOOTING:
        double ready = 1;
        if (turretIO.getExpectedDelta() > 0.01)
          ready -= Math.abs(turretIO.getExpectedDelta()) * 50; // TODO: Tweak Constants
        if (hoodIO.getExpectedDelta() > 0.01) ready -= Math.abs(hoodIO.getExpectedDelta()) * 50;
        if (flywheelIO.getExpectedDelta() > 1) ready -= Math.abs(flywheelIO.getExpectedDelta()) / 5;
        if (ready < 0) ready = 0;

        if (ready >= 0.995) Presets.SolidColor(ColorPalette.Green);
        else Presets.SolidColor(ColorPalette.Crossfade(ColorPalette.Red, ColorPalette.Yellow, ready));
      case CLIMBING:
        Presets.Strobe(ColorPalette.Green, 0.25);
      case IDLE:
        Presets.Fade(ColorPalette.White, 10);
      case ESTOP:
        Presets.Fade(ColorPalette.Red, 0.5);
      case ERROR:
        Presets.SolidColor(ColorPalette.Yellow);
    }
  }

  //#region Animation States
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

  //#region Animation Presets
  static class Presets {
    // Uniform
    public static void SolidColor(RGBWColor color) {
      LEDSegment.ClearAnimIDs(0, 7);
      LEDSegment.All.setSolidColor(color);
    }
    public static void Strobe(RGBWColor color, double period) {
      LEDSegment.ClearAnimIDs(1, 7);
      LEDSegment.All.setStrobeAnimation(color, period);
    }
    public static void Fade(RGBWColor color, double period) {
      LEDSegment.ClearAnimIDs(1, 7);
      LEDSegment.All.setFadeAnimation(color, period);
    }
    public static void RGBFade(double period) {
      LEDSegment.ClearAnimIDs(1, 7);
      LEDSegment.All.setRGBFadeAnimation(period);
    }

    // Non-Uniform
    public static void Fire(boolean inverted) { // Flow to center (edges if inverted)
      for (LEDSegment s : LightsConstants.OuterSegments) s.setFireAnimation(0.4, 0.4, inverted, 40);
      for (LEDSegment s : LightsConstants.InnerSegments) s.setFireAnimation(0.4, 0.4, inverted, 40);
    }
    public static void RainbowSingle(boolean inverted) { // Flow to left (right if inverted)
      for (LEDSegment s : LightsConstants.QuadsRight) s.setRainbowAnimation(1, inverted);
      for (LEDSegment s : LightsConstants.QuadsLeft) s.setRainbowAnimation(1, !inverted);
    }
    public static void RainbowSplit(boolean inverted) { // Flow to center (edges if inverted)
      for (LEDSegment s : LightsConstants.AllQuadSegments) s.setRainbowAnimation(1, inverted);
    }
    public static void FlowIndividualMerging(RGBWColor color, double period, boolean inverted) { // Flow to quad centers (quad edges if inverted)
      for (LEDSegment s : LightsConstants.OuterSegments) s.setFlowAnimation(color, period, inverted);
      for (LEDSegment s : LightsConstants.InnerSegments) s.setFlowAnimation(color, period, !inverted);
    }
    public static void FlowIndividualSynced(RGBWColor color, double period, boolean inverted) { // Flow to center (edges if inverted)
      for (LEDSegment s : LightsConstants.OuterSegments) s.setFlowAnimation(color, period, inverted);
    }
    public static void FlowQuads(RGBWColor color, double period, boolean inverted) { // Flow to center (edges if inverted)
      for (LEDSegment s : LightsConstants.AllQuadSegments) s.setFlowAnimation(color, period, inverted);
    }

    // // Shooter Interrupt
    // static boolean shooterInterrupted = false;
    // public static void releaseShooter() { shooterInterrupted = false; }
    // public static void interruptShooter() {
    //   shooterInterrupted = true;
    //   for (LEDSegment s : LightsConstants.ShooterSegments)
    // }
  }
  
  //#region Segmentation

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
