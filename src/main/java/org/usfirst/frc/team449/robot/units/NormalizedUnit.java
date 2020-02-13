package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @param <USelf> the CRTP
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public abstract class NormalizedUnit<USelf extends NormalizedUnit<USelf>> {
    private final double value;

    public NormalizedUnit(final double value) {
        this.value = value;
    }

    // TODO Figure out how to actually get a normalize() method that returns the SI base unit
    public final double getNormalizedValue() {
        return this.getRawValue() * this.getUnit().getRawValue();
    }

    /**
     * The actual unit value of this unit, if that makes sense.
     */
    // TODO Is there some way to convert everything to interfaces and create a "UnitUnit" interface that we can anonymously implement to mark unit value objects?
    public abstract USelf getUnit();

    @Override
    public abstract String toString();

    protected final double getRawValue() {
        return this.value;
    }

    protected abstract int getPrecedence();

    protected final String valueOfConsideringPrecedence(final NormalizedUnit other) {
        return other.getPrecedence() > this.getPrecedence() ? other.toString() : "(" + other.toString() + ")";
    }
}
