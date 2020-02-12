package org.usfirst.frc.team449.robot.units;

public class Meter extends DistanceUnit<Meter> {
    public Meter(final double value){
        super(value);
    }

    public Meter(final int value){
        this((double)value);
    }

    @Override
    protected Meter getUnit() {
        return new Meter(1);
    }
}
