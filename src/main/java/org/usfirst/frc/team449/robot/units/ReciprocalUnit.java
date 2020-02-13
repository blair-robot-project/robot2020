package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReciprocalUnit<U extends NormalizedUnit<U, UN>, UN extends NormalizedUnit<UN, UN>> extends CombinationUnit<ReciprocalUnit<U, UN>, ReciprocalUnit<UN, UN>> {
    private final U unit;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ReciprocalUnit(final U unit) {
        this.unit = unit;
    }

    @Override
    public ReciprocalUnit<UN, UN> getNormalized() {
        return new ReciprocalUnit<>(this.unit.getNormalized());
    }

    @Override
    public double getNormalizedValue() {
        return 1.0 / this.unit.getNormalizedValue();
    }

    /**
     * Polymorphic prototype constructor.
     *
     * @param value
     * @return
     */
    @Override
    public ReciprocalUnit<U, UN> withValue(final double value) {
        return new ReciprocalUnit<>(this.unit.withValue(value));
    }

    @Override
    public String toString() {
        return String.format("%s^-1", this.valueOfConsideringPrecedence(this.unit));
    }

    @Override
    protected int getPrecedence() {
        return 2;
    }
}
