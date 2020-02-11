package org.usfirst.frc.team449.robot.units;

public class Minute extends TimeUnit {
    public Minute(final double value) {
        super(value * 60.0);
    }

    public Minute(final int value) {
        this((double) value);
    }
}
