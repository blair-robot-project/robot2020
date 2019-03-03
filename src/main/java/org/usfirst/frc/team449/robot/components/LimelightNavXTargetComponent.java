package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedAHRS;
import java.util.function.DoubleSupplier;

public class LimelightNavXTargetComponent implements DoubleSupplier {

    @NotNull
    private final MappedAHRS ahrs;

    @JsonCreator
    public LimelightNavXTargetComponent(@NotNull @JsonProperty (required = true) MappedAHRS ahrs){
        this.ahrs = ahrs;
    }

    @Override
    public double getAsDouble() {
        return -Math.IEEEremainder(ahrs.getCachedHeading(), 45)/2.;
    }
}
