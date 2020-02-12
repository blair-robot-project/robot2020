package org.usfirst.frc.team449.robot.units;

public class DimensionlessUnit extends NormalizedUnit<DimensionlessUnit> {
    public DimensionlessUnit(final double value) {
        super(value);
    }

    public DimensionlessUnit(final int value) {
        this((double) value);
    }

    @Override
    protected DimensionlessUnit getUnit() {
        return new DimensionlessUnit(1);
    }
}
