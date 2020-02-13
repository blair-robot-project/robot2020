package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Minute extends TimeUnit<Minute> {
    public Minute(final double value) {
        super(value);
    }

    public Minute(final int value) {
        this((double) value);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public static Minute s_getUnit() {
        return UNIT;
    }

    private static final Minute UNIT = new Minute(60);
    @Override
    public Minute getUnit() {
        return UNIT;
    }

    @Override
    public String getShortUnitName() {
        return "min";
    }
}
