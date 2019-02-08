package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.function.DoubleSupplier;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LimeLightAngularToRelevantComponent extends LimeLightComponent implements DoubleSupplier {

    @JsonCreator
    public LimeLightAngularToRelevantComponent(@JsonProperty(required = true) ReturnValue value){
       super(value);
    }

    @Override
    public double getAsDouble() {
        double returnAngle = super.getAsDouble();
        return 0;
    }
}
