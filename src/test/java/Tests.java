import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;

public final class Tests {
    @Test
    public void deserializeMap() {
        Robot.loadMap();
        System.out.println("*******************************************\nMAP DESERIALIZATION SUCCESSFUL\n*******************************************");
    }
}
