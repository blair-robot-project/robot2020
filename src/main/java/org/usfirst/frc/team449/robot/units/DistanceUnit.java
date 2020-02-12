package org.usfirst.frc.team449.robot.units;

public abstract class DistanceUnit<USelf extends DistanceUnit<USelf>> extends NormalizedUnit<DistanceUnit<USelf>> {
    public DistanceUnit(final double value) {
        super(value);
    }
}
