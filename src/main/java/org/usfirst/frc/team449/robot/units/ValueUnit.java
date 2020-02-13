package org.usfirst.frc.team449.robot.units;

public abstract class ValueUnit<USelf extends ValueUnit<USelf>> extends NormalizedUnit<ValueUnit<USelf>> {
    public ValueUnit(final double value) {
        super(value);
    }

    public abstract String getShortUnitName();

    @Override
    public String toString() {
        return String.format("%s %s", this.getRawValue(), this.getShortUnitName());
    }

    @Override
    protected int getPrecedence() {
        return Integer.MAX_VALUE;
    }
}
