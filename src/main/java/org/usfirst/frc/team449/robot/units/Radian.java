package org.usfirst.frc.team449.robot.units;

public class Radian extends AngleUnit<Radian> {
    public Radian(final double value) {
        super(value);
    }

    public Radian(final int value) {
        this((double) value);
    }

    @Override
    public Radian withValue(final double value) {
        return new Radian(value);
    }

    private static final Radian UNIT = new Radian(1);

    @Override
    public Radian getUnit() {
        return UNIT;
    }

    @Override
    public String getShortUnitName() {
        return "rad";
    }
}
