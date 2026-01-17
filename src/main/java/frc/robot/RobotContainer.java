// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.hopper.HopperSubsystem;
import frc.robot.subsystems.lights.LightsSubsystem;
import frc.robot.subsystems.turret.TurretSubsystem;
import frc.robot.subsystems.vision.VisionSubsystem;
import frc.robot.subsystems.controls.ControlsSubsystem;

public class RobotContainer {
  public RobotContainer() {
    configureBindings();
  }

  private List<SubsystemBase> subsystems = new ArrayList<>();

  private void configureBindings() {
    // Comment out subsystems from here to activate/deactivate them
    
    // Side note: All subsystems define suppliers in their constructors, 
    // so attempting to reach data from an inactive subsystem is safe.
    //
    // Do not initialize suppliers (to anything but warning messages) outside of constructors.

    // Vision
    subsystems.add(new VisionSubsystem());

    // Piece Managment
    subsystems.add(new IntakeSubsystem());
    subsystems.add(new HopperSubsystem());
    subsystems.add(new TurretSubsystem());

    // Climber
    subsystems.add(new ClimberSubsystem());

    // Lights
    subsystems.add(new LightsSubsystem());

    // Controls
    subsystems.add(new ControlsSubsystem());
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
