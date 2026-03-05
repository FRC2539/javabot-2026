package frc.robot.subsystems.lights;

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

public enum LEDSegment {
  // NOTE: All segments point towards the middle of the robot (meaning between the center and shooter segments) by default

  // Individual
  ShooterTop(0, true, 20), // TODO: Sizing
  RightTop(1, true, 17),
  RightBottom(2, false, 17),
  ShooterBottom(3, false, 20),
  CenterBottom(4, true, 12),
  LeftBottom(5, true, 20),
  LeftTop(6, false, 20),
  CenterTop(7, false, 12),

  // All
  All(0, false, ShooterTop.startIndex, CenterTop.endIndex),

  // Thirds
  ThirdTopRight(0, true, ShooterTop.startIndex, RightTop.endIndex),
  ThirdBottom(1, false, RightBottom.startIndex, LeftBottom.endIndex),
  ThirdTopLeft(2, false, LeftTop.startIndex, CenterTop.endIndex),

  // Quads
  QuadRightTop(0, true, ShooterTop.startIndex, RightTop.endIndex),
  QuadRightBottom(1, false, RightBottom.startIndex, ShooterBottom.endIndex),
  QuadLeftBottom(2, true, CenterBottom.startIndex, LeftBottom.endIndex),
  QuadLeftTop(3, false, LeftTop.startIndex, CenterTop.endIndex);
  
  public static final CANdle candle = LightsSubsystem.candle;
  public final int animSlot;
  public final boolean reversed;
  public final int segmentSize;
  public final int startIndex;
  public final int endIndex;

  LEDSegment(int animSlot, boolean reversed, int size) {
    this.animSlot = animSlot;
    this.reversed = reversed;
    segmentSize = size;
    startIndex = LightsConstants.lastIndex;
    LightsConstants.lastIndex += size;
    endIndex = LightsConstants.lastIndex - 1;
  }
  LEDSegment(int animSlot, boolean reversed, int startIndex, int endIndex) {
    this.animSlot = animSlot;
    this.reversed = reversed;
    segmentSize = endIndex - startIndex + 1;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public static void ClearAnimIDs(int min, int max) {
    for (int i = min; i <= max; i++) LightsConstants.AllSegments[i].clearAnimation();
  } 

  // #region Animations

  public void clearAnimation() {
    candle.setControl(new EmptyAnimation(animSlot));
  }

  public void setSolidColor(RGBWColor color) {
    candle.setControl(new EmptyAnimation(animSlot));
    candle.setControl(
        new SolidColor(startIndex, endIndex)
            .withColor(color));
  }

  public void setStrobeAnimation(RGBWColor color, double periodSeconds) {
    double frameRateHz = 2.0 / periodSeconds; // Hz of 2 = 1 cycle per second
    candle.setControl(
        new StrobeAnimation(startIndex, endIndex)
            .withColor(color)
            .withSlot(animSlot)
            .withFrameRate(frameRateHz));
  }

  public void setFadeAnimation(RGBWColor color, double periodSeconds) {
    double frameRateHz = 200.0 / periodSeconds; // Hz of 200 = 1 cycle per second
    candle.setControl(
        new SingleFadeAnimation(startIndex, endIndex)
            .withColor(color)
            .withFrameRate(frameRateHz)
            .withSlot(animSlot));
  }

  public void setRGBFadeAnimation(double periodSeconds) {
    double frameRateHz = 600.0 / periodSeconds; // Hz of 600 = 1 cycle per second
    candle.setControl(
        new RgbFadeAnimation(startIndex, endIndex)
            .withFrameRate(frameRateHz)
            .withSlot(animSlot));
  }

  public void setRainbowAnimation(double periodSeconds, boolean inverted) {
    double frameRateHz = 120.0 / periodSeconds; // Hz of 120 = 1 cycle per second
    candle.setControl(
        new RainbowAnimation(startIndex, endIndex)
            .withFrameRate(frameRateHz)
            .withDirection(
                (reversed != inverted) ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
            .withSlot(animSlot));
  }

  public void setFlowAnimation(RGBWColor color, double periodSeconds, boolean inverted) {
    double frameRateHz =
        2.0 * segmentSize / periodSeconds; // Hz of 2 = 1 cycle per second
    candle.setControl(new ColorFlowAnimation(startIndex, endIndex)
            .withColor(color)
            .withFrameRate(frameRateHz)
            .withDirection(
                (reversed != inverted) ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
            .withSlot(animSlot));
  }

  public void setLarsonAnimation(RGBWColor color, double periodSeconds, int size) {
    size = Math.min(size, 15);
    double frameRateHz =
        2.0 * (segmentSize - size) / periodSeconds; // Hz of 2 = 1 cycle per second
    candle.setControl(
        new LarsonAnimation(startIndex, endIndex)
            .withColor(color)
            .withFrameRate(frameRateHz)
            .withSize(size)
            .withBounceMode(LarsonBounceValue.Front)
            .withSlot(animSlot));
  }

  public void setFireAnimation(double cooling, double sparking, boolean inverted, double fps) {
    candle.setControl(
        new FireAnimation(startIndex, endIndex)
            .withCooling(cooling)
            .withSparking(sparking)
            .withFrameRate(fps)
            .withDirection(
                (reversed != inverted) ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
            .withSlot(animSlot));
  }
}