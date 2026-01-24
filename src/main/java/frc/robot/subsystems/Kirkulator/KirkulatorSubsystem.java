package frc.robot.subsystems.Kirkulator;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.KirkulatorConstants;
import frc.robot.subsystems.Kirkulator.KirkulatorIO.KirkulatorIOInputs;

import org.littletonrobotics.junction.Logger;

public class KirkulatorSubsystem extends SubsystemBase {

  private KirkulatorIO kirkulatorIO;

  private static final double passiveVoltage = 2;
  private static final double shootingVoltage = 8;

  public KirkulatorSubsystem(KirkulatorIO kirkulatorIO) {
    this.kirkulatorIO = kirkulatorIO;
    setDefaultCommand(setVoltage(passiveVoltage));

    Command dynamicIdleCommand =
        Commands.run(
                () -> {
                  double idleVoltage =
                          Intaking() ? shootingVoltage : passiveVoltage;

                  kirkulatorIO.setVoltage(idleVoltage);
                },
                this)
            .withName("idle");

    setDefaultCommand(dynamicIdleCommand);
  }

  @Override
  public void periodic() {
  }

  public Command setVoltage() {
    return Commands.race(
        setVoltage(shootingVoltage));
  }

  public boolean Intaking() {
    return false;
  }

  public Command setVoltagePassive() {
    return Commands.race(
        setVoltage(passiveVoltage));
  }

  public Command setVoltage(double voltage) {
    return Commands.run(
        () -> {
          kirkulatorIO.setVoltage(voltage);
        },
        this);
  }

  public Command setReverse(double voltage) {
    return Commands.run(
        () -> {
          kirkulatorIO.setVoltage(-voltage);
        },
        this);
  }

}