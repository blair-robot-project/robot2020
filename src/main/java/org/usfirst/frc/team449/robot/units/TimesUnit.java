package org.usfirst.frc.team449.robot.units;

public class TimesUnit<
        U1 extends NormalizedUnit<U1, UN1>,
        UN1 extends NormalizedUnit<UN1, UN1>,
        U2 extends NormalizedUnit<U2, UN2>,
        UN2 extends NormalizedUnit<UN2, UN2>>
        extends CombinationUnit<TimesUnit<U1, UN1, U2, UN2>, TimesUnit<UN1, UN1, UN2, UN2>> {

    private final U1 first;
    private final U2 second;

    public TimesUnit(final U1 first, final U2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public TimesUnit<UN1, UN1, UN2, UN2> getNormalized() {
        return new TimesUnit<>(this.first.getNormalized(), this.second.getNormalized());
    }

    @Override
    public double getNormalizedValue() {
        return this.first.getNormalizedValue() * this.second.getNormalizedValue();
    }

    /**
     * Polymorphic prototype constructor.
     *
     * @param value
     * @return
     */
    @Override
    public TimesUnit<U1, UN1, U2, UN2> withValue(final double value) {
        return new TimesUnit<>(this.first.withValue(value), this.second.getUnit());
    }

    @Override
    public String toString() {
        return String.format("%s * %s", this.valueOfConsideringPrecedence(this.first), this.valueOfConsideringPrecedence(this.second));
    }

    @Override
    protected int getPrecedence() {
        return 1;
    }
}
