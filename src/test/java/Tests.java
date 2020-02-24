import org.junit.Assert;
import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;

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
}
