package org.usfirst.frc.team449.robot.units;

public class Revolution extends AngleUnit<Revolution> {
    public Revolution(final double value) {
        super(value);
    }

    public Revolution(final int value) {
        this((double) value);
    }

    @Override
    public Revolution getUnit() {
        return new Revolution(1);
    }

    @Override
    public String getShortUnitName() {
        return "rev";
    }
}
