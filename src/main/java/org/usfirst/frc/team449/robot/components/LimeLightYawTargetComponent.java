package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.LinearFilter;

import java.util.function.DoubleSupplier;

public class LimeLightYawTargetComponent implements DoubleSupplier {
    LimeLightComponent limeLightComponent;
    LinearFilter filter;

    @JsonCreator
    public LimeLightYawTargetComponent(@JsonProperty(required = true) Integer bufferCapacity, @JsonProperty (required = true) LimeLightComponent limeLightComponent) {
        this.limeLightComponent = limeLightComponent;
        filter = LinearFilter.movingAverage(bufferCapacity != null ? bufferCapacity : 5);
    }

    @Override
    public double getAsDouble() {
        return filter.calculate(this.limeLightComponent.getAsDouble());
    }
}
