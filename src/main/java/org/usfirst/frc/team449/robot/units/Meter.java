package org.usfirst.frc.team449.robot.units;

public class Meter extends DisplacementUnit<Meter> {
    public Meter(final double value){
        super(value);
    }

    public Meter(final int value){
        this((double)value);
    }

    @Override
    public Meter getUnit() {
        return new Meter(1);
    }

    @Override
    public String getShortUnitName() {
        return "m";
    }
}
