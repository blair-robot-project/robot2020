package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public abstract class NormalizedUnit {
    private final double normalizedValue;

    public NormalizedUnit(final double normalizedValue){
        this.normalizedValue = normalizedValue;
    }

    public final double getNormalizedValue() {
        return this.normalizedValue;
    }
}
