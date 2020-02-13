package org.usfirst.frc.team449.robot.units;

public class DimensionlessUnit extends BaseUnit<DimensionlessUnit> {
    public DimensionlessUnit(final double value) {
        super(value);
    }

    public DimensionlessUnit(final int value) {
        this((double) value);
    }

    private static final DimensionlessUnit UNIT = new DimensionlessUnit(1);

    @Override
    public DimensionlessUnit getUnit() {
        return UNIT;
    }

    /**
     * Polymorphic prototype constructor.
     *
     * @param value
     * @return
     */
    @Override
    public DimensionlessUnit withValue(final double value) {
        return new DimensionlessUnit(value);
    }

    @Override
    public String getShortUnitName() {
        return "u";
    }
}
