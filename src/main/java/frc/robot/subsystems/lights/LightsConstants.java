package frc.robot.subsystems.lights;

import com.ctre.phoenix6.signals.RGBWColor;

public class LightsConstants {
  //#region Constants
  public static int CandleID = 10;
  public static int StripSize = 200;
  public static int lastIndex = 0;
  
  //#region Segments
  public static LEDSegment[] RightSegments = { LEDSegment.RightBottom, LEDSegment.RightTop };
  public static LEDSegment[] ShooterSegments = { LEDSegment.ShooterBottom, LEDSegment.ShooterTop };
  public static LEDSegment[] CenterSegments = { LEDSegment.CenterBottom, LEDSegment.CenterTop };
  public static LEDSegment[] LeftSegments = { LEDSegment.LeftBottom, LEDSegment.LeftTop };

  public static LEDSegment[] InnerSegments = { LEDSegment.ShooterTop, LEDSegment.ShooterBottom, LEDSegment.CenterBottom, LEDSegment.CenterTop };
  public static LEDSegment[] OuterSegments = { LEDSegment.RightTop, LEDSegment.RightBottom, LEDSegment.LeftBottom, LEDSegment.LeftTop };

  public static LEDSegment[] AllSegments = { LEDSegment.ShooterTop, LEDSegment.RightTop, LEDSegment.RightBottom, LEDSegment.ShooterBottom, LEDSegment.CenterBottom, LEDSegment.LeftBottom, LEDSegment.LeftTop, LEDSegment.CenterTop };
  public static LEDSegment[] NotShooterSegments = { LEDSegment.RightTop, LEDSegment.RightBottom, LEDSegment.CenterBottom, LEDSegment.LeftBottom, LEDSegment.LeftTop, LEDSegment.CenterTop };
  
  public static LEDSegment[] QuadsLeft = {LEDSegment.QuadLeftTop, LEDSegment.QuadLeftBottom};
  public static LEDSegment[] QuadsRight = {LEDSegment.QuadRightTop, LEDSegment.QuadRightBottom};
  public static LEDSegment[] QuadsTop = {LEDSegment.QuadLeftTop, LEDSegment.QuadRightTop};
  public static LEDSegment[] QuadsBottom = {LEDSegment.QuadLeftBottom, LEDSegment.QuadRightBottom};
  
  public static LEDSegment[] AllQuadSegments = { LEDSegment.QuadRightTop, LEDSegment.QuadRightBottom, LEDSegment.QuadLeftBottom, LEDSegment.QuadLeftTop };
  
  //#region Colors
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
