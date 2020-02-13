package org.usfirst.frc.team449.robot.units;

// TODO Why not extend ValueUnit<DisplacementUnit<USelf>>?
public abstract class DisplacementUnit<USelf extends DisplacementUnit<USelf>> extends ValueUnit<USelf> {
    public DisplacementUnit(final double value) {
        super(value);
    }
}
