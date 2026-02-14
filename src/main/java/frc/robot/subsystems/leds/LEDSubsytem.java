package frc.robot.subsystems.leds;

import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
import com.ctre.phoenix6.controls.SingleFadeAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsytem extends SubsystemBase{

    public CANdle leds = new CANdle(LEDConstants.CandleID);


    public enum AnimationTypes{
        INTAKING,
        SHOOTING,
        CLIMBING,
        ALIGNED,
        VICTORY
    }

    public void runAnimation(AnimationTypes type, RGBWColor color){
        switch (type) {
            case INTAKING:
                runStrobeAnimation(color);
                break;
            case SHOOTING:
                runStrobeAnimation(color);
                break;
            case CLIMBING:  
                runColorFlowAnimation(color);
                break;
            case ALIGNED:
                runSingleFadeAnimation(color);
                break;
            case VICTORY:
                runRainbowAnimation();
                break;
            default:
                setSolidColor(color);
                break;
        }
    }


    public void setSolidColor(RGBWColor color){
        SolidColor solidColor = new SolidColor(0, LEDConstants.LedCount - 1).withColor(color);

        leds.setControl(solidColor);
    }

    public void runStrobeAnimation(RGBWColor color){
        StrobeAnimation strobeAnimation = new StrobeAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        leds.setControl(strobeAnimation);
    }

    public void runFireAnimation(){
        FireAnimation fireAnimation = new FireAnimation(0, LEDConstants.LedCount - 1);

        leds.setControl(fireAnimation);
    }

    public void runColorFlowAnimation(RGBWColor color){
        ColorFlowAnimation colorFlowAnimation = new ColorFlowAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        leds.setControl(colorFlowAnimation);
    }

    public void runLarsonAnimation(RGBWColor color){
        LarsonAnimation larsonAnimation = new LarsonAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        leds.setControl(larsonAnimation);
    }

    public void runRainbowAnimation(){
        RainbowAnimation rainbowAnimation = new RainbowAnimation(0, LEDConstants.LedCount - 1);

        leds.setControl(rainbowAnimation);
    }

    public void runRGBFadeAnimation(){
        RgbFadeAnimation rgbFadeAnimation = new RgbFadeAnimation(0, LEDConstants.LedCount - 1);

        leds.setControl(rgbFadeAnimation);
    }

    public void runSingleFadeAnimation(RGBWColor color){
        SingleFadeAnimation singleFadeAnimation = new SingleFadeAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        leds.setControl(singleFadeAnimation);
    }

    public void runTwinkleAnimation(RGBWColor color){
        TwinkleAnimation twinkleAnimation = new TwinkleAnimation(0, LEDConstants.LedCount - 1).withColor(color);
    }
}