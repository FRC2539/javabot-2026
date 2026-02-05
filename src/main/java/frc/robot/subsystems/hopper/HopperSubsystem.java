// package frc.robot.subsystems.hopper;

// import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import org.littletonrobotics.junction.Logger;

// public class HopperSubsystem extends SubsystemBase {

//   private final HopperIO io;
//   private final HopperIOInputsAutoLogged inputs = new HopperIOInputsAutoLogged();

//   public HopperSubsystem(HopperIO io) {
//     this.io = io;
//   }

//   public Command setIntakePosition(IntakePosition position) {
//     return runOnce(() -> io.setIntakeSolenoid(position.value));
//   }

//   public Command setRaspberryPosition(raspberryPosition position) {
//     return runOnce(() -> io.setRaspberrySolenoid(position.value));
//   }

//   public Command setRaspberry2Position(raspberry2Position position) {
//     return runOnce(() -> io.setRaspberry2Solenoid(position.value));
//   }

//   @Override
//   public void periodic() {
//     io.updateInputs(inputs);
//     Logger.processInputs("RealOutputs/Pneumatics", inputs);
//   }
// }
