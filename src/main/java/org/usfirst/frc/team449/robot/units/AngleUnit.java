package org.usfirst.frc.team449.robot.units;

public abstract class AngleUnit<USelf extends AngleUnit<USelf>> extends ValueUnit<USelf> {
    public AngleUnit(final double normalizedValue) {
        super(normalizedValue);
    }
}
