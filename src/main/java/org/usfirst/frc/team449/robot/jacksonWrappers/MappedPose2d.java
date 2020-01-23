package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedPose2d extends Pose2d {

    /**
     * Pose2d wrapper for Trajectory loading from map
     *
     * @param xPosition The absolute x position in feet
     * @param yPosition The absolute y position in feet
     * @param angle     The angle at this position in degrees
     */
    @JsonCreator
    public MappedPose2d(@JsonProperty(required = true) double xPosition,
                        @JsonProperty(required = true) double yPosition,
                        double angle){
        super(xPosition / 3.281, yPosition / 3.281, new Rotation2d(Math.toRadians(angle)));
    }
}
