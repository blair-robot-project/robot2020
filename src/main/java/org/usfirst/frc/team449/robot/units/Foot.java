package org.usfirst.frc.team449.robot.units;

import edu.wpi.first.wpilibj.util.Units;

public class Foot extends DistanceUnit<Foot> {
    public Foot(final double value) {
        super(value);
    }

    public Foot(final int value) {
        this((double) value);
    }

    @Override
    protected Foot getUnit() {
        return new Foot(Units.feetToMeters(1));
    }
}
