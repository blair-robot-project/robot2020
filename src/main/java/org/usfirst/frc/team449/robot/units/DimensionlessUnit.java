package org.usfirst.frc.team449.robot.units;

public class DimensionlessUnit extends NormalizedUnit {
    public DimensionlessUnit(final double value) {
        super(value);
    }

    public DimensionlessUnit(final int value) {
        this((double) value);
    }
}
