// package frc.robot.commands;

// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.DriverStation.Alliance;
// import edu.wpi.first.wpilibj.Timer;

// public class HubShiftUtil {
//   private enum ShiftEnum {
//     TRANSITION,
//     SHIFT1,
//     SHIFT2,
//     SHIFT3,
//     SHIFT4,
//     ENDGAME,
//     AUTO,
//     DISABLED;
//   }

//   private record ShiftInfo(
//       ShiftEnum currentShift, double elapsedTime, double remainingTime, boolean active) {}

//   private static Timer shiftTimer = new Timer();
//   private static final ShiftEnum[] shiftsEnums = ShiftEnum.values();

//   private static final double[] shiftStartTimes = {0.0, 10.0, 35.0, 60.0, 85.0, 110.0};
//   private static final double[] shiftEndTimes = {10.0, 35.0, 60.0, 85.0, 110.0, 140.0};
//   private static final double autoEndTime = 20.0;
//   private static final boolean[] activeSchedule = {true, true, false, true, false, true};
//   private static final boolean[] inactiveSchedule = {true, false, true, false, true, true};

//   private static Alliance getFirstActiveAlliance() {
//     String message = DriverStation.getGameSpecificMessage();
//     if (message.length() > 0) {
//       char character = message.charAt(0);
//       if (character == 'R') {
//         return Alliance.Blue;
//       } else if (character == 'B') {
//         return Alliance.Red;
//       }
//     }
//     return DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue
//         ? Alliance.Red
//         : Alliance.Blue;
//   }

//   public static void initialize() {
//     shiftTimer.restart();
//   }

//   public static ShiftInfo getShiftInfo() {
//     double currentTime = shiftTimer.get();
//     boolean[] currentSchedule = inactiveSchedule;
//     Alliance startAlliance = getFirstActiveAlliance();
//     currentSchedule =
//         startAlliance == DriverStation.getAlliance().orElse(Alliance.Blue)
//             ? activeSchedule
//             : inactiveSchedule;
//     double stateTimeElapsed = 0.0;
//     double stateTimeRemaining = 0.0;
//     boolean active = false;
//     ShiftEnum currentShift = ShiftEnum.DISABLED;

//     if (DriverStation.isAutonomous()) {
//       stateTimeElapsed = currentTime;
//       stateTimeRemaining = autoEndTime - currentTime;
//       active = true;
//       currentShift = ShiftEnum.AUTO;
//     } else if (DriverStation.isEnabled()) {
//       int currentShiftIndex = -1;
//       for (int i = 0; i < shiftStartTimes.length; i++) {
//         if (currentTime >= shiftStartTimes[i] && currentTime < shiftEndTimes[i]) {
//           currentShiftIndex = i;
//           break;
//         }
//       }
//       if (currentShiftIndex < 0) {
//         // After last shift, so assume endgame
//         currentShiftIndex = shiftStartTimes.length - 1;
//       }

//       // Calculate elapsed and remaining time in the current shift, ignoring combined shifts
//       stateTimeElapsed = currentTime - shiftStartTimes[currentShiftIndex];
//       stateTimeRemaining = shiftEndTimes[currentShiftIndex] - currentTime;

//       if (currentShiftIndex > 0) {
//         if (currentSchedule[currentShiftIndex] == currentSchedule[currentShiftIndex - 1]) {
//           stateTimeElapsed = currentTime - shiftStartTimes[currentShiftIndex - 1];
//         }
//       }
//       if (currentShiftIndex < shiftEndTimes.length - 1) {
//         if (currentSchedule[currentShiftIndex] == currentSchedule[currentShiftIndex + 1]) {
//           stateTimeRemaining = shiftEndTimes[currentShiftIndex + 1] - currentTime;
//         }
//       }

//       active = currentSchedule[currentShiftIndex];
//       currentShift = shiftsEnums[currentShiftIndex];
//     }
//     ShiftInfo shiftInfo = new ShiftInfo(currentShift, stateTimeElapsed, stateTimeRemaining, active);
//     return shiftInfo;
//   }
// }
