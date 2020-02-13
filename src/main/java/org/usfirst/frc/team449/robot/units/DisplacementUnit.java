package org.usfirst.frc.team449.robot.units;

// TODO Why not extend BaseUnit<DisplacementUnit<USelf>, Meter>?
public abstract class DisplacementUnit<USelf extends DisplacementUnit<USelf>> extends BaseUnit<USelf, Meter> {
    public DisplacementUnit(final double value) {
        super(value);
    }

    @Override
    public final Meter getNormalized() {
        return new Meter(this.getNormalizedValue());
    }
}
