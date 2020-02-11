package org.usfirst.frc.team449.robot.units;

// Doesn't work.
public class UnitRateUnit<U1 extends NormalizedUnit, U2 extends NormalizedUnit> extends DividedUnit<U1, U2> {
    public UnitRateUnit(final U1 top) {
        super(top, (U2) new DimensionlessUnit(1));
    }
}
