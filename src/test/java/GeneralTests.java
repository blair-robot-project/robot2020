import org.junit.Assert;
import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;

<<<<<<< .merge_file_a02908
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
=======
public final class GeneralTests extends TestHostBase {
    /**
     * Verifies that the map can be deserialized without errors.
     */
    @Test
    public void deserializeMap() {
        Assert.assertNotNull(Robot.loadMap());
        System.out.println("*******************************************\nMAP DESERIALIZATION SUCCESSFUL\n*******************************************");
    }
>>>>>>> .merge_file_a12768
}
