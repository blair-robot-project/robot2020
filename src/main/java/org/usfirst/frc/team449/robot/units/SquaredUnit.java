package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SquaredUnit<U extends NormalizedUnit<U>> extends TimesUnit<U, U> {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public SquaredUnit(final U unit) {
        super(unit, unit.getUnit());
    }
}
