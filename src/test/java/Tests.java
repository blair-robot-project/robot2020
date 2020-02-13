import org.junit.Assert;
import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;
import org.usfirst.frc.team449.robot.RobotMap;
import org.usfirst.frc.team449.robot.SimulationConfig;

public final class Tests {
    @org.junit.Before
    public void before() {
        Robot.notifyTesting();
    }

    @Test
    public void deserializeMap() {
        Assert.assertNotNull(Robot.loadMap(RobotMap.class, Robot.MAP_PATH));
        Assert.assertNotNull(Robot.loadMap(SimulationConfig.class, SimulationConfig.MAP_PATH));
        System.out.println("*******************************************\nMAP DESERIALIZATION SUCCESSFUL\n*******************************************");
    }
}
