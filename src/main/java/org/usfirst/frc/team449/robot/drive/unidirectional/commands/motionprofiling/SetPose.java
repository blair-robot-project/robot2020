package org.usfirst.frc.team449.robot.drive.unidirectional.commands.motionprofiling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.usfirst.frc.team449.robot.auto.commands.AutonomousCommand;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class SetPose<T extends DriveUnidirectionalWithGyro> extends InstantCommand implements AutonomousCommand {
    T drive;

    @JsonCreator
    public SetPose(@JsonProperty(required = true) Pose2d initalPose){
        drive.resetOdometry(initalPose);
    }
}
