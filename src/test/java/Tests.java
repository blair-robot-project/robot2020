import edu.wpi.first.wpilibj2.command.Command;
import org.junit.Assert;
import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;

import java.util.function.Consumer;

public final class Tests {
  @org.junit.Before
  public void before() {
    Robot.notifyTesting();
  }

  @Test
  public void deserializeMap() {
    Assert.assertNotNull(Robot.loadMap());
    System.out.println("*******************************************\nMAP DESERIALIZATION SUCCESSFUL\n*******************************************");
  }

  @Test
  public void test() {
    final Consumer<Command> method = Command::schedule;
    Assert.assertNotNull(method);
  }
}
