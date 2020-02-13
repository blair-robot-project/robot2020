package org.usfirst.frc.team449.robot.units;

public abstract class TimeUnit<USelf extends TimeUnit<USelf>> extends BaseUnit<USelf> {
    public TimeUnit(final double normalizedValue) {
        super(normalizedValue);
    }
}
