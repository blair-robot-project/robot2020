package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Squares the unit but not the value.
 * TODO Probably need to split up toString to handle units like this more cleanly (e.g. in order to put parens around the unit name only)
 *
 * @param <U>
 */
public class SquaredUnit<U extends NormalizedUnit<U>> extends TimesUnit<U, U> {
    private final U unit;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public SquaredUnit(final U unit) {
        super(unit, unit.getUnit());
        this.unit = unit;
    }

    @Override
    public String toString() {
        return String.format("%s^2", this.valueOfConsideringPrecedence(this.unit));
    }

    @Override
    protected int getPrecedence() {
        return 2;
    }
}
