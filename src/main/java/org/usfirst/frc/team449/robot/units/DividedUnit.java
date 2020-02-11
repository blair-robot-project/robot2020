package org.usfirst.frc.team449.robot.units;

/**
 * This is pretty cool, isn't it?
 *
 * @param <U1>
 * @param <U2>
 */
public class DividedUnit<U1 extends NormalizedUnit, U2 extends NormalizedUnit> extends TimesUnit<U1, ReciprocalUnit<U2>> {
    public DividedUnit(final U1 top, final U2 bottom) {
        super(top, new ReciprocalUnit<>(bottom));
    }
}
