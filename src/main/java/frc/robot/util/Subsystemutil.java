package frc.robot.util;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.ArrayList;
import java.util.List;

public abstract class Subsystemutil extends SubsystemBase {
  private static List<Subsystemutil> instances = new ArrayList<>();

  public Subsystemutil() {
    super();
    instances.add(this);
  }

  public Subsystemutil(String name) {
    super(name);
    instances.add(this);
  }

  public abstract void periodicAfterScheduler();

  public static void runAllPeriodicAfterScheduler() {
    for (Subsystemutil instance : instances) {
      instance.periodicAfterScheduler();
    }
  }
}
