package org.usfirst.frc.team449.robot.units;

public class TimesUnit<U1 extends NormalizedUnit, U2 extends NormalizedUnit> extends NormalizedUnit {
    public TimesUnit(final U1 first, final U2 second) {
        super(first.getNormalizedValue() * second.getNormalizedValue());
    }
}
