package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj2.command.Subsystem;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public abstract class InstantCommandMixIn {
    // TODO: Test this
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InstantCommandMixIn(@JsonProperty(required = true) final Runnable toRun,
                               final Subsystem... requirements) {
    }
}
