package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.filters.LinearDigitalFilter;

import java.util.function.DoubleSupplier;

public class LimeLightYawTargetComponent implements DoubleSupplier {
    LimeLightComponent limeLightComponent;
    LinearDigitalFilter filter;

    @JsonCreator
    public LimeLightYawTargetComponent(@JsonProperty(required = true) Integer bufferCapacity, @JsonProperty (required = true) LimeLightComponent limeLightComponent) {
        this.limeLightComponent = limeLightComponent;
        filter = LinearDigitalFilter.movingAverage(new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return null;
            }

            @Override
            public double pidGet() {
                return limeLightComponent.getAsDouble();
            }
        }, bufferCapacity != null ? bufferCapacity : 5);
    }

    @Override
    public double getAsDouble() {
        return filter.get();
    }
}
