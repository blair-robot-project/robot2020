package org.usfirst.frc.team449.robot.units;

public class Second extends TimeUnit<Second> {
    public Second(final double value) {
        super(value);
    }

    public Second(final int value) {
        this((double)value);
    }

    @Override
    public Second getUnit() {
        return new Second(1);
    }

    @Override
    public String getShortUnitName() {
        return "s";
    }
}
