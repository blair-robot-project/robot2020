package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.util.Units;

public class MappedTranslation2d {

    public Translation2d translation;

    /**
     * Pose2d wrapper for Trajectory loading from map
     *
     * @param xPosition The absolute x position in meters
     * @param yPosition The absolute y position in meters
     */
    @JsonCreator
    public MappedTranslation2d(@JsonProperty(required = true) double xPosition,
                        @JsonProperty(required = true) double yPosition){
        translation = new Translation2d(xPosition, yPosition);
    }
}
