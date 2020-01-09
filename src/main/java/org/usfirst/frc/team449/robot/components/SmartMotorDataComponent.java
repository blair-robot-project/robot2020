package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.generalInterfaces.smartMotor.SmartMotor;

public class SmartMotorDataComponent {
    @NotNull private final SmartMotor motor;
    @NotNull private final ReturnValue value;

    public enum ReturnValue {
        position, velocity, current, voltage;
    }

    /**
     *
     * @param motor the motor to get a value from
     * @param value whether to get the position, velocity, current, or voltage
     */
    @JsonCreator
    public SmartMotorDataComponent(@NotNull @JsonProperty(required = true) SmartMotor motor,
                              @NotNull @JsonProperty(required = true) ReturnValue value) {
        this.motor = motor;
        this.value = value;
    }

    /**
     * @return the requested value from the motor - null if none specified or if
     *                      the value itself is null in the motor
     */
    public Double getAsDouble() {
        switch(value) {
            case position:
                return motor.getPositionFeet();
            case velocity:
                return motor.getVelocity();
            case current:
                return motor.getOutputCurrent();
            case voltage:
                return motor.getOutputVoltage();
            default:
                return null;
        }
    }
}
