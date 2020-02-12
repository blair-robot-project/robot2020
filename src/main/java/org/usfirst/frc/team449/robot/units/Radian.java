package org.usfirst.frc.team449.robot.units;

public class Radian extends AngleUnit<Radian> {
    public Radian(final double value) {
        super(value);
    }

    public Radian(final int value) {
        this((double) value);
    }

    @Override
    protected Radian getUnit() {
        return new Radian(0.5 / Math.PI);
    }
}
