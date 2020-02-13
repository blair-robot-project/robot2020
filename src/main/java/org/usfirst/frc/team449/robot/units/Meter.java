package org.usfirst.frc.team449.robot.units;

public class Meter extends DisplacementUnit<Meter> {
    public Meter(final double value) {
        super(value);
    }

    public Meter(final int value) {
        this((double) value);
    }

    @Override
    public Meter withValue(final double value) {
        return new Meter(value);
    }

    private static final Meter UNIT = new Meter(1);

    @Override
    public Meter getUnit() {
        return UNIT;
    }

    @Override
    public String getShortUnitName() {
        return "m";
    }
}
