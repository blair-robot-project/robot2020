import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner.ControlPanelColor;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner.commands._2020SpinControlPanelUntilSatisfied;

import static org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner.ControlPanelColor.*;

/**
 * Sanity checks for {@link _2020SpinControlPanelUntilSatisfied#getOptimalSpinDirection(ControlPanelColor, ControlPanelColor)}.
 */
@RunWith(Parameterized.class)
public class GetOptimalSpinDirectionTests extends TestHostBase {
    private final ControlPanelColor from;
    private final ControlPanelColor to;
    private final boolean expected;

    public GetOptimalSpinDirectionTests(final ControlPanelColor from, final ControlPanelColor to, final boolean expected) {
        this.from = from;
        this.to = to;
        this.expected = expected;
    }

    @Test
    public void verify() {
        Assert.assertEquals(this.expected, _2020SpinControlPanelUntilSatisfied.getOptimalSpinDirection(this.from, this.to));
    }

    @Parameterized.Parameters(name = "{index}: {0}->{1}: {2}")
    public static Object[][] parameters() {
        return new Object[][] {
                {RED, GREEN, true},
                {RED, YELLOW, false},
                {GREEN, BLUE, true},
                {GREEN, RED, false},
                {BLUE, YELLOW, true},
                {BLUE, GREEN, false},
                {YELLOW, RED, true},
                {YELLOW, BLUE, false},

                {RED, BLUE, true},
                {BLUE, RED, true},
                {GREEN, YELLOW, true},
                {YELLOW, GREEN, true},

                {RED, RED, true},
                {BLUE, BLUE, true},
                {GREEN, GREEN, true},
                {YELLOW, YELLOW, true},
        };
    }
}
