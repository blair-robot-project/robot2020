package org.usfirst.frc.team449.robot.units;

/**
 * A unit that represents a dimension.
 *
 * @param <USelf>
 */
public abstract class BaseUnit<USelf extends BaseUnit<USelf, UNormal>, UNormal extends BaseUnit<UNormal, UNormal>> extends NormalizedUnit<BaseUnit<USelf, UNormal>, UNormal> {
    private final double value;

    public BaseUnit(final double value) {
        this.value = value;
    }

    public abstract String getShortUnitName();

    @Override
    public double getNormalizedValue() {
        return this.getRawValue() * this.getUnit().getRawValue();
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.getRawValue(), this.getShortUnitName());
    }

    /**
     * Gets the value of this instance in terms of {@link BaseUnit#getUnit()}.
     *
     * @return
     */
    protected final double getRawValue() {
        return this.value;
    }

    @Override
    protected final int getPrecedence() {
        return Integer.MAX_VALUE;
    }
}
