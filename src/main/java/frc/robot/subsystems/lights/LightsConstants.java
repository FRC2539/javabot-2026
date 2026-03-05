package frc.robot.subsystems.lights;

import com.ctre.phoenix6.signals.RGBWColor;

public class LightsConstants {

  public static int CandleID = 10;
  public static int StripSize = 200;

  public static int StartIndex = 0;

  public static class ColorPalette {
    static RGBWColor Orange = new RGBWColor(240, 50, 10);
    static RGBWColor Red = new RGBWColor(200, 0, 0);
    static RGBWColor Yellow = new RGBWColor(230, 200, 20);
    static RGBWColor Green = new RGBWColor(0, 235, 0);
    static RGBWColor Blue = new RGBWColor(0, 0, 230);
    static RGBWColor White = new RGBWColor(200, 200, 200);

    static RGBWColor Crossfade(RGBWColor a, RGBWColor b, double ratio) {
      return new RGBWColor(
          (int) (a.Red * (1 - ratio) - b.Red),
          (int) (a.Green * (1 - ratio) - b.Green),
          (int) (a.Blue * (1 - ratio) - b.Blue));
    }
  }
}
