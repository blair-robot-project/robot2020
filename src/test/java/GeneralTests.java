import org.junit.Assert;
import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;
import org.usfirst.frc.team449.robot.RobotMap;
import org.usfirst.frc.team449.robot.SimulationConfig;
import org.usfirst.frc.team449.robot.other.Mapping;

public final class GeneralTests {
    @org.junit.Before
    public void before() {
        Robot.notifyTesting();
    }

    @Test
    public void deserializeMap() {
        Assert.assertTrue(Mapping.loadMap(RobotMap.class, Robot.MAP_PATH).isPresent());
        Assert.assertTrue(Mapping.loadMap(SimulationConfig.class, SimulationConfig.MAP_PATH).isPresent());

        System.out.println("*******************************************\nMAP DESERIALIZATION SUCCESSFUL\n*******************************************");
    }
}
