package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.commands.AnalogPIDCommand;

import java.util.function.DoubleSupplier;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LimeLightAngularToRelevantComponent implements DoubleSupplier {

    @NotNull
    AnalogPIDCommand anComd;

    @JsonCreator
    public LimeLightAngularToRelevantComponent(@JsonProperty(required = true) AnalogPIDCommand anComd){
        this.anComd = anComd;


    }

    @Override
    public double getAsDouble() {
        return 0;
    }
}
