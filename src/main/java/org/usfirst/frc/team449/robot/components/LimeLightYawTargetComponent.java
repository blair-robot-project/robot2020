package org.usfirst.frc.team449.robot.components;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.filters.LinearDigitalFilter;

import java.util.function.DoubleSupplier;

public class LimeLightYawTargetComponent implements DoubleSupplier {
    LimeLightComponent limeLightComponent;
    LinearDigitalFilter filter;

    public LimeLightYawTargetComponent(int bufferCapacity, LimeLightComponent limeLightComponent) {
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
        }, bufferCapacity);
    }

    @Override
    public double getAsDouble() {
        return filter.get();
    }
}
