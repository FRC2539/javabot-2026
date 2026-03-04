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
import frc.robot.subsystems.shooter.flywheel.FlywheelIO;
import frc.robot.subsystems.shooter.hood.HoodIO;
import frc.robot.subsystems.shooter.turret.TurretIO;
import java.util.function.BooleanSupplier;

public class LightsSubsystem extends SubsystemBase {

  public CANdle candle = new CANdle(LightsConstants.CandleID);

  public LightsSubsystem() {
    CANdleConfiguration cfg = new CANdleConfiguration();
    cfg.CANdleFeatures.StatusLedWhenActive = StatusLedWhenActiveValue.Disabled;
    cfg.LED.StripType = StripTypeValue.GRB;
    cfg.LED.BrightnessScalar = 1.0;
    candle.getConfigurator().apply(cfg);
  }

  static class ColorPalette {
    static RGBWColor Orange = new RGBWColor(240, 79, 37);
    static RGBWColor Red = new RGBWColor(240, 2, 2);
    static RGBWColor Yellow = new RGBWColor(232, 198, 28);
    static RGBWColor Green = new RGBWColor(37, 235, 30);
    static RGBWColor Blue = new RGBWColor(30, 88, 235);
    static RGBWColor White = new RGBWColor(200, 200, 200);

    static RGBWColor Crossfade(RGBWColor a, RGBWColor b, double ratio) {
      return new RGBWColor(
          (int) (a.Red * (1 - ratio) - b.Red),
          (int) (a.Green * (1 - ratio) - b.Green),
          (int) (a.Blue * (1 - ratio) - b.Blue));
    }
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
    switch (getAnimationState()) {
      case DISABLED:
        if (gameEnded) setRainbowAnimation(2, false);
        else setFlowAnimation(ColorPalette.Orange, 3, false);
        gameEnded = false;
      case PLAYING:
        if (InputSubsystem.currentMatchTimeframe.duration - InputSubsystem.MatchTimeframeTimer.get()
                <= 3
            && InputSubsystem.getWillActivitySwap())
          setFadeAnimation(
              (InputSubsystem.IsHubActive()) ? ColorPalette.Orange : ColorPalette.Yellow, 1);
        else
          setSolidColor((InputSubsystem.IsHubActive()) ? ColorPalette.Orange : ColorPalette.Yellow);
        if (InputSubsystem.currentMatchTimeframe == MatchTimeframe.EndGame) gameEnded = true;
      case INTAKING:
        setFadeAnimation(ColorPalette.Blue, 0.5);
      case SHOOTING:
        double ready = 1;
        if (turretIO.getExpectedDelta() > 0.01) ready -= Math.abs(turretIO.getExpectedDelta()) * 50;
        if (hoodIO.getExpectedDelta() > 0.01) ready -= Math.abs(hoodIO.getExpectedDelta()) * 50;
        if (flywheelIO.getExpectedDelta() > 1) ready -= Math.abs(flywheelIO.getExpectedDelta()) / 5;
        if (ready < 0) ready = 0;

        if (ready >= 0.995) setSolidColor(ColorPalette.Green);
        else setSolidColor(ColorPalette.Crossfade(ColorPalette.Red, ColorPalette.Yellow, ready));
      case CLIMBING:
        setStrobeAnimation(ColorPalette.Green, 0.25);
      case IDLE:
        setFadeAnimation(ColorPalette.White, 10);
      case ESTOP:
        setStrobeAnimation(ColorPalette.Red, 0.25);
      case ERROR:
        setSolidColor(ColorPalette.Yellow);
    }
  }

  boolean gameEnded = false;
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

  // #region Animations

  public void setSolidColor(RGBWColor color) {
    candle.setControl(new EmptyAnimation(0));
    candle.setControl(
        new SolidColor(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withColor(color));
  }

  public void setStrobeAnimation(RGBWColor color, double periodSeconds) {
    double frameRateHz = 2.0 / periodSeconds; // Hz of 2 = 1 cycle per second
    candle.setControl(
        new StrobeAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withColor(color)
            .withSlot(0)
            .withFrameRate(frameRateHz));
  }

  public void setFadeAnimation(RGBWColor color, double periodSeconds) {
    double frameRateHz = 200.0 / periodSeconds; // Hz of 200 = 1 cycle per second
    candle.setControl(
        new SingleFadeAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withColor(color)
            .withFrameRate(frameRateHz)
            .withSlot(0));
  }

  public void setRGBFadeAnimation(double periodSeconds) {
    double frameRateHz = 600.0 / periodSeconds; // Hz of 600 = 1 cycle per second
    candle.setControl(
        new RgbFadeAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withFrameRate(frameRateHz)
            .withSlot(0));
  }

  public void setRainbowAnimation(double periodSeconds, boolean inverted) {
    double frameRateHz = 120.0 / periodSeconds; // Hz of 120 = 1 cycle per second
    candle.setControl(
        new RainbowAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withFrameRate(frameRateHz)
            .withDirection(
                (inverted) ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
            .withSlot(0));
  }

  public void setFlowAnimation(RGBWColor color, double periodSeconds, boolean inverted) {
    double frameRateHz =
        2.0 * LightsConstants.StripSize / periodSeconds; // Hz of 2 = 1 cycle per second
    ColorFlowAnimation flow =
        new ColorFlowAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withColor(color)
            .withFrameRate(frameRateHz)
            .withDirection(
                (inverted) ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
            .withSlot(0);
    candle.setControl(flow);
  }

  public void setLarsonAnimation(RGBWColor color, double periodSeconds, int size) {
    size = Math.min(size, 15);
    double frameRateHz =
        2.0 * (LightsConstants.StripSize - size) / periodSeconds; // Hz of 2 = 1 cycle per second
    candle.setControl(
        new LarsonAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withColor(color)
            .withFrameRate(frameRateHz)
            .withSize(size)
            .withBounceMode(LarsonBounceValue.Front)
            .withSlot(0));
  }

  public void setFireAnimation(double cooling, double sparking, boolean inverted, double fps) {
    candle.setControl(
        new FireAnimation(
                LightsConstants.StartIndex,
                LightsConstants.StartIndex + LightsConstants.StripSize - 1)
            .withCooling(cooling)
            .withSparking(sparking)
            .withFrameRate(fps)
            .withDirection(
                (inverted) ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
            .withSlot(0));
  }

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
