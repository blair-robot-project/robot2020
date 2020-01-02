package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;

/**
 * Motor feed forward calculator using velocity and acceleration
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedFeedForwardCalculator extends SimpleMotorFeedforward {

    /**
     * Default constructor.
     *
     * @param kS The static gain.
     * @param kV The velocity gain.
     * @param kA The acceleration gain.
     */
    @JsonCreator
    public MappedFeedForwardCalculator(@JsonProperty(required = true) double kS,
                                @JsonProperty(required = true) double kV,
                                @JsonProperty(required = true) double kA) {
        super(kS, kV, kA);
    }
}
