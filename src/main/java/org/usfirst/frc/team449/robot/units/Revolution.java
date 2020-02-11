package org.usfirst.frc.team449.robot.units;

public class Revolution extends AngleUnit {
    public Revolution(final double value) {
        super(value);
    }

    public Revolution(final int value) {
        this((double) value);
    }
}
