package org.usfirst.frc.team449.robot.units;

public class DimensionlessUnit extends ValueUnit<DimensionlessUnit> {
    public DimensionlessUnit(final double value) {
        super(value);
    }

    public DimensionlessUnit(final int value) {
        this((double) value);
    }

    @Override
    public DimensionlessUnit getUnit() {
        return new DimensionlessUnit(1);
    }

    @Override
    public String getShortUnitName() {
        return "u";
    }
}
