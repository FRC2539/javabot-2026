package frc.robot.subsystems.transporter;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.LogTable;


public interface TransportIO {

    void updateInputs(TransportIOInputs inputs);

    @AutoLog
    public class TransportIOInputs implements org.littletonrobotics.junction.inputs.LoggableInputs {
        public double transportVoltage = 0.0;

        @Override
        public void toLog(LogTable table) {
            table.put("transportVoltage", transportVoltage);
        }

        @Override
        public void fromLog(LogTable table) {
            transportVoltage = table.get("transportVoltage", 0.0); 
        }
    }

    void setTransportVoltage(double volts);
}