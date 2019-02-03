package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;

import java.util.function.DoubleSupplier;

public class TalonDataComponent implements DoubleSupplier {
    @NotNull FPSTalon talon;
    @NotNull ReturnValue value;

    enum ReturnValue{
        position, velocity, current, voltage;
    }

    /**
     *
     * @param talon the talon to get a value from
     * @param value whether to get the postition, velocity, current, or voltage
     */
    @JsonCreator
    public TalonDataComponent(@JsonProperty(required = true) FPSTalon talon,
                              @JsonProperty(required = true) ReturnValue value) {
        this.talon = talon;
        this.value = value;
    }

    /**
     * @return the requested value from the talon - 0 if none specified
     */
    @Override
    public double getAsDouble() {
        switch(value) {
            case position:
                return talon.getPositionFeet();
                break;
            case velocity:
                return talon.getVelocity();
                break;
            case current:
                return talon.getOutputCurrent();
                break;
            case voltage:
                return talon.getOutputVoltage();
                break;
            default:
                return 0;
                break;
        }
    }
}
