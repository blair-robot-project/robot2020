package org.usfirst.frc.team449.robot.units;

public class Revolution extends AngleUnit<Revolution> {
    public Revolution(final double value) {
        super(value);
    }

    public Revolution(final int value) {
        this((double) value);
    }

    @Override
    public Revolution withValue(final double value) {
        return new Revolution(value);
    }

    private static final Revolution UNIT = new Revolution(2 * Math.PI);

    @Override
    public Revolution getUnit() {
        return UNIT;
    }

    @Override
    public String getShortUnitName() {
        return "rev";
    }
}
