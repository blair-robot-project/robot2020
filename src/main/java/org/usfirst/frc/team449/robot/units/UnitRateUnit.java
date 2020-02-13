package org.usfirst.frc.team449.robot.units;

public class UnitRateUnit<U1 extends NormalizedUnit<U1, UN1>, UN1 extends NormalizedUnit<UN1, UN1>, U2 extends NormalizedUnit<U2, UN2>, UN2 extends NormalizedUnit<UN2, UN2>> extends DividedUnit<U1, U2> {
    public UnitRateUnit(final U1 top, final U2 bottom) {
        super(top, bottom.getUnit());
        if (bottom.getNormalizedValue() != bottom.getUnit().getNormalizedValue()) {
            throw new IllegalArgumentException("bottom must be a unit unit");
        }
    }
}
