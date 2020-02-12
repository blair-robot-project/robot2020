package org.usfirst.frc.team449.robot.units;

public class Minute extends TimeUnit<Minute> {
    public Minute(final double value) {
        super(value);
    }

    public Minute(final int value) {
        this((double) value);
    }

    @Override
    protected Minute getUnit() {
        return new Minute(60);
    }
}
