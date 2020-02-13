package org.usfirst.frc.team449.robot.units;

public class TimesUnit<U1 extends NormalizedUnit<U1>, U2 extends NormalizedUnit<U2>> extends NormalizedUnit<TimesUnit<U1, U2>> {
    private final U1 first;
    private final U2 second;

    public TimesUnit(final U1 first, final U2 second) {
        super(first.getRawValue() * second.getRawValue());
        this.first = first;
        this.second = second;
    }

    @Override
    public TimesUnit<U1, U2> getUnit() {
        return new TimesUnit<>(this.first.getUnit(), this.second.getUnit());
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
