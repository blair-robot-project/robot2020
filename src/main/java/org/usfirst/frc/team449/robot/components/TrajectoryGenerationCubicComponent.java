package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedTranslationSet;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public class TrajectoryGenerationCubicComponent implements TrajectoryGenerationComponent {

    TrajectoryConstraint constraint;
    TrajectoryConfig configuration;
    MappedTranslationSet translations;
    Trajectory trajectory;

    @JsonCreator
    public TrajectoryGenerationCubicComponent(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                              @JsonProperty(required = true) double maxSpeedMeters,
                                              @JsonProperty(required = true) double maxAccelMeters,
                                              @JsonProperty(required = true) MappedTranslationSet waypoints){
        constraint = new DifferentialDriveVoltageConstraint(
                drivetrain.getLeftFeedforwardCalculator(),
                drivetrain.getDriveKinematics(),
                12);

        // Create config for trajectory
        configuration = new TrajectoryConfig(maxSpeedMeters, maxAccelMeters)
                .setKinematics(drivetrain.getDriveKinematics())
                .addConstraint(constraint);

        this.translations = waypoints;
    }

    @Override
    public Trajectory getTrajectory(){
        trajectory = TrajectoryGenerator.generateTrajectory(translations.getStartingPose(),
                translations.getTranslations(),
                translations.getEndingPose(),
                configuration);
        return trajectory;
    }
}
