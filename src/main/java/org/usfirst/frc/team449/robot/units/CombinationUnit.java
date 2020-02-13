package org.usfirst.frc.team449.robot.units;

public abstract class CombinationUnit<
        USelf extends CombinationUnit<USelf, UNormal>,
        UNormal extends CombinationUnit<UNormal, UNormal>>
        extends NormalizedUnit<USelf, UNormal> {

    @Override
    public USelf getUnit() {
        return this.withValue(1.0);
    }
}
