package org.usfirst.frc.team449.robot.units;

public class Second extends TimeUnit {
    public Second(final double value) {
        super(value);
    }

    public Second(final int value) {
        this((double)value);
    }
}
