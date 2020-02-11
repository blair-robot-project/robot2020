package org.usfirst.frc.team449.robot.units;

public class Meter extends DistanceUnit {
    public Meter(final double value){
        super(value);
    }

    public Meter(final int value){
        this((double)value);
    }
}
