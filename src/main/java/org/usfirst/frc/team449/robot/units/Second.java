package org.usfirst.frc.team449.robot.units;

public class Second extends TimeUnit<Second> {
    public Second(final double value) {
        super(value);
    }

    public Second(final int value) {
        this((double)value);
    }

    @Override
    protected Second getUnit() {
        return new Second(1);
    }
}
