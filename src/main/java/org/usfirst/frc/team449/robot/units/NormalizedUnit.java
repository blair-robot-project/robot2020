package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public abstract class NormalizedUnit<USelf extends NormalizedUnit<USelf>> {
    private final double value;

    public NormalizedUnit(final double value) {
        this.value = value;
    }

    public double getRawValue() {
        return this.value;
    }

    public final double getNormalizedValue() {
        return this.value * this.getUnit().getRawValue();
    }

    protected abstract USelf getUnit();
}
