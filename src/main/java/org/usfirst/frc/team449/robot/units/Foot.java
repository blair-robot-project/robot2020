package org.usfirst.frc.team449.robot.units;

import edu.wpi.first.wpilibj.util.Units;

public class Foot extends DistanceUnit {
    public Foot(final double value) {
        super(Units.feetToMeters(value));
    }

    public Foot(final int value) {
        this((double) value);
    }
}
