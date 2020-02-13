package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is pretty cool, isn't it?
 *
 * @param <U1>
 * @param <U2>
 */
public class DividedUnit<U1 extends NormalizedUnit<U1>, U2 extends NormalizedUnit<U2>> extends TimesUnit<U1, ReciprocalUnit<U2>> {
    private final U1 top;
    private final U2 bottom;

    // Apparently JsonCreator is only required if any of the parameters have @JsonProperty
    @JsonCreator
    public DividedUnit(@JsonProperty(required = true) final U1 top, @JsonProperty(required = true) final U2 bottom) {
        super(top, new ReciprocalUnit<>(bottom));
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return String.format("%s / %s", this.valueOfConsideringPrecedence(this.top), this.valueOfConsideringPrecedence(this.bottom));
    }

    @Override
    protected int getPrecedence() {
        return 1;
    }
}
