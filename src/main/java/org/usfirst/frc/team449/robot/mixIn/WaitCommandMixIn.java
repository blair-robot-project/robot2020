package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class WaitCommandMixIn {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public WaitCommandMixIn(@JsonProperty(value = "timeout", required = true) final double seconds) {
    }
}
