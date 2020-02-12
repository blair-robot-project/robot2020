package org.usfirst.frc.team449.robot.units;

public class Revolution extends AngleUnit<Revolution> {
    public Revolution(final double value) {
        super(value);
    }

    public Revolution(final int value) {
        this((double) value);
    }

    @Override
    protected Revolution getUnit() {
        return new Revolution(1);
    }
}
