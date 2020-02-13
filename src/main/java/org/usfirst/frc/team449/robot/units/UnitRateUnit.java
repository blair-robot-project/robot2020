package org.usfirst.frc.team449.robot.units;

public class UnitRateUnit<U1 extends NormalizedUnit<U1>, U2 extends NormalizedUnit<U2>> extends DividedUnit<U1, U2> {
    public UnitRateUnit(final U1 top, final U2 bottom) {
        super(top, bottom.getUnit());
        if (bottom != bottom.getUnit()) {
            throw new IllegalArgumentException("bottom must be a unit unit");
        }
    }
}
