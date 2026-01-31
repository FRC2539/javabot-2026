// package frc.robot.subsytems.indexer;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.subsytems.indexer.IndexerIO.IndexerIOInputs;

// public class IndexerSubsystem extends SubsystemBase{

//     IndexerIO indexerIO;
//     IndexerIOInputs inputs = new IndexerIOInputs();

//     public IndexerSubsystem(IndexerIO indexerIO){
//         this.indexerIO = indexerIO;
//     }

//     public void periodic(){
//         indexerIO.updateInputs(inputs);
//         // Logger.processInputs("RealOutputs/Indexer", inputs);
//     }

//     public Command setVoltage(double voltage){
//         return Commands.run(
//           () -> indexerIO.setVoltage(voltage)
//         );
//     }
// }
