package org.usfirst.frc.team449.robot.units;

import edu.wpi.first.wpilibj.util.Units;

public class Foot extends DisplacementUnit<Foot> {
    public Foot(final double value) {
        super(value);
    }

    public Foot(final int value) {
        this((double) value);
    }

    @Override
    public Foot withValue(final double value) {
        return new Foot(value);
    }

    @Override
    public Foot getUnit() {
        return new Foot(Units.feetToMeters(1));
    }

    @Override
    public String getShortUnitName() {
        return "ft";
    }
}
