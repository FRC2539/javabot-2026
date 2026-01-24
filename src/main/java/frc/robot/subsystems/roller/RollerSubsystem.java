package frc.robot.subsystems.roller;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;

import org.littletonrobotics.junction.Logger;

public class RollerSubsystem extends SubsystemBase {

  private static final LoggedTunableNumber setRollerVoltage =
      new LoggedTunableNumber("/roller/RollerVoltage", 12.0);

//#region instantiate
  private RollerIO rollerIO;
  private RollerIOInputsAutoLogged rollerInputs = new RollerIOInputsAutoLogged();
  private IntakeState state = IntakeState.DISABLED;


  public RollerSubsystem(RollerIO rollerIO) {
    this.rollerIO = rollerIO;

    setDefaultCommand(Commands.parallel(setRollerVoltage(0)));
  }
  //#region State kinds
  public enum IntakeState {
        DISABLED,
        INTAKING,
        EJECTING,
        EXPANDING,
        CLOSINGHOP,
        SHOOTING //likley uneeded
    }


  public Command setRollerVoltage(double voltage) {
    return Commands.run(
        () -> {
          rollerIO.setRollerVoltage(voltage);
        },
        this);
  }
      


  @Override
  public void periodic() {
    rollerIO.updateInputs(rollerInputs);

   Logger.processInputs("RealOutputs/Roller", rollerInputs);
//#region state cases
    switch (state) {
            case DISABLED:
                setRollerVoltage(0);
                //setPivotVoltage(0);
                break;
            case INTAKING:
                setRollerVoltage(0);
                //setPivotVoltage(0);
                break;
            case EJECTING:
                setRollerVoltage(0);
                //setPivotVoltage(0);
                break;
            case EXPANDING:
                setRollerVoltage(0);
                //setPivotVoltage(0);
                break;
            case CLOSINGHOP:
                setRollerVoltage(0);
                //setPivotVoltage(0);
                break;
            case SHOOTING: //likley uneeded
                setRollerVoltage(0);
                //setPivotVoltage(0);
                break;
        }
  }
//#region case adjustments
      public Command disabledCommand() {
        return runEnd(
                () -> {
                    setIntakeState(IntakeState.DISABLED);
                },
                () -> {});
    }

    public Command intakingCommand() {
        return runEnd(
                () -> {
                    setIntakeState(IntakeState.INTAKING);
                },
                () -> {});
    }

    public Command ejectingCommand() {
        return runEnd(
                () -> {
                    setIntakeState(IntakeState.EJECTING);
                },
                () -> {});
    }

    public Command expandingCommand() {
        return runEnd(
                () -> {
                    setIntakeState(IntakeState.EXPANDING);
                },
                () -> {});
    }

    public Command intakeShootAdjustmentsCommand() { //likley uneeded
        return runEnd(
                () -> {
                    setIntakeState(IntakeState.SHOOTING);
                },
                () -> {});
    }

    public Command closingHopperCommand() {
        return runEnd(
                () -> {
                    setIntakeState(IntakeState.CLOSINGHOP);
                },
                () -> {});
    }

    public void setIntakeState(IntakeState state) {
        this.state = state;
    }


}