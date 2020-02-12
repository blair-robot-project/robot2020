package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReciprocalUnit<U extends NormalizedUnit<U>> extends NormalizedUnit<ReciprocalUnit<U>> {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ReciprocalUnit(final U unit) {
        super(1.0 / unit.getRawValue());
        this.unit = unit;
    }

    @Override
    protected ReciprocalUnit<U> getUnit() {
        return new ReciprocalUnit<>(this.unit.getUnit());
    }

    private final U unit;
}
