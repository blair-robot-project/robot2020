import org.junit.Before;
import org.usfirst.frc.team449.robot.Robot;

/**
 * Test classes should all extend this class to ensure that the robot is notified before the first
 * test runs.
 */
public abstract class TestHostBase {
  @Before
  public void before() {
    Robot.notifyTesting();
  }
}
