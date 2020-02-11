package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReciprocalUnit<U extends NormalizedUnit> extends NormalizedUnit {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ReciprocalUnit(final U unit) {
        super(1.0 / unit.getNormalizedValue());
    }
}
