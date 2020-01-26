package org.usfirst.frc.team449.robot.commands.multiInterface.drive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.components.TrajectoryGenerationComponent;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedPose2d;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RamseteControllerUnidirectionalDrive extends RamseteCommand {

    @JsonCreator
    public RamseteControllerUnidirectionalDrive(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                                @JsonProperty(required = true) MappedPose2d startingPose,
                                                @JsonProperty(required = true) double P,
                                                @JsonProperty(required = true) double D,
                                                @JsonProperty(required = true) TrajectoryGenerationComponent trajectoryGenerator){
        super(trajectoryGenerator.getTrajectory(startingPose),
                drivetrain::getCurrentPose,
                new RamseteController(),
                drivetrain.getLeftFeedforwardCalculator(),
                drivetrain.getDriveKinematics(),
                drivetrain::getWheelSpeeds,
                new PIDController(P, 0, D),
                new PIDController(P, 0, D),
                drivetrain::setVoltage);

        addRequirements(drivetrain);
    }
}
