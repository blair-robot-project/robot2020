package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @param <USelf>   the CRTP
 * @param <UNormal> the SI base units for the dimensions that this unit represents
 */
// TODO: Can we get rid of the wildcard somehow?
// UNormal extends NormalizedUnit<UNormal, UNormal>
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public abstract class NormalizedUnit<USelf extends NormalizedUnit<USelf, UNormal>, UNormal> implements NormalizableUnit<UNormal> {
    @Override
    public abstract UNormal getNormalized();

    public abstract double getNormalizedValue();

    /**
     * The actual unit value of this unit, if that makes sense.
     *
     * <p>
     * The raw value of this instance represents the number that this unit is multiplied by to convert to the SI base unit for its dimension.
     * </p>
     */
    // TODO Is there some way to convert everything to interfaces and create a "UnitUnit" interface that we can anonymously implement to mark unit value objects?
    public abstract USelf getUnit();

    /**
     * Polymorphic prototype constructor.
     *
     * @param value
     * @return
     */
    public abstract USelf withValue(double value);

    /**
     * Formats this instance in a nice, human-readable format.
     *
     * @return
     */
    @Override
    public abstract String toString();

    protected abstract int getPrecedence();

    protected final String valueOfConsideringPrecedence(final NormalizedUnit other) {
        return other.getPrecedence() > this.getPrecedence() ? other.toString() : "(" + other.toString() + ")";
    }
}
