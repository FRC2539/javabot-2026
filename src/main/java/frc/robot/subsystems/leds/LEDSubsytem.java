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

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsytem extends SubsystemBase{

    public CANdle leds = new CANdle(LEDConstants.CandleID);

    public LEDSubsytem(){

        setDefaultCommand(setSolidColor(new RGBWColor(240, 79, 37)));
    }


    public enum AnimationTypes{
        INTAKING,
        SHOOTING,
        CLIMBING,
        ALIGNED,
        VICTORY
    }

    public Command runAnimation(AnimationTypes type, RGBWColor color){
        switch (type) {
            case INTAKING:
                return runStrobeAnimation(color);
            case SHOOTING:
                return runStrobeAnimation(color);
            case CLIMBING:  
                return runColorFlowAnimation(color);
            case ALIGNED:
                return runSingleFadeAnimation(color);
            case VICTORY:
                return runRainbowAnimation();
            default:
                return setSolidColor(color);
        }
    }


    public Command setSolidColor(RGBWColor color){
        SolidColor solidColor = new SolidColor(0, LEDConstants.LedCount - 1).withColor(color);

        return Commands.run( () -> leds.setControl(solidColor));
    }

    public Command runStrobeAnimation(RGBWColor color){
        StrobeAnimation strobeAnimation = new StrobeAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        return Commands.run( () -> leds.setControl(strobeAnimation));
    }

    public Command runFireAnimation(){
        FireAnimation fireAnimation = new FireAnimation(0, LEDConstants.LedCount - 1);

        return Commands.run( () -> leds.setControl(fireAnimation));
    }

    public Command runColorFlowAnimation(RGBWColor color){
        ColorFlowAnimation colorFlowAnimation = new ColorFlowAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        return Commands.run( () -> leds.setControl(colorFlowAnimation));
    }

    public Command runLarsonAnimation(RGBWColor color){
        LarsonAnimation larsonAnimation = new LarsonAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        return Commands.run( () -> leds.setControl(larsonAnimation));
    }

    public Command runRainbowAnimation(){
        RainbowAnimation rainbowAnimation = new RainbowAnimation(0, LEDConstants.LedCount - 1);

        return Commands.run( () -> leds.setControl(rainbowAnimation));
    }

    public Command runRGBFadeAnimation(){
        RgbFadeAnimation rgbFadeAnimation = new RgbFadeAnimation(0, LEDConstants.LedCount - 1);

        return Commands.run( () -> leds.setControl(rgbFadeAnimation));
    }

    public Command runSingleFadeAnimation(RGBWColor color){
        SingleFadeAnimation singleFadeAnimation = new SingleFadeAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        return Commands.run( () -> leds.setControl(singleFadeAnimation));
    }

    public Command runTwinkleAnimation(RGBWColor color){
        TwinkleAnimation twinkleAnimation = new TwinkleAnimation(0, LEDConstants.LedCount - 1).withColor(color);

        return Commands.run( () -> leds.setControl(twinkleAnimation));
    }
}