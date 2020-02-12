package org.usfirst.frc.team449.robot.units;

public class TimesUnit<U1 extends NormalizedUnit<U1>, U2 extends NormalizedUnit<U2>> extends NormalizedUnit<TimesUnit<U1, U2>> {
    public TimesUnit(final U1 first, final U2 second) {
        super(first.getRawValue() * second.getRawValue());
        this.first = first;
        this.second = second;
    }

    @Override
    protected TimesUnit<U1, U2> getUnit() {
        return new TimesUnit<>(this.first.getUnit(), this.second.getUnit());
    }

    private final U1 first;
    private final U2 second;
}
