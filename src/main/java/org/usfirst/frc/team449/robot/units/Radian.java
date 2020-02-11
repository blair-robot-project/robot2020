package org.usfirst.frc.team449.robot.units;

public class Radian extends AngleUnit {
    public Radian(final double value) {
        super(value * 0.5 / Math.PI);
    }

    public Radian(final int value) {
        this((double) value);
    }
}
