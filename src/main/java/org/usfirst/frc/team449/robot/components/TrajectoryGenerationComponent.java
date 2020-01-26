package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public class TrajectoryGenerationComponent {

    TrajectoryConstraint constraint;
    TrajectoryConfig configuration;
    List<Pose2d> waypoints;
    Trajectory trajectory;

    @JsonCreator
    public TrajectoryGenerationComponent(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                         @JsonProperty(required = true) double maxSpeedMeters,
                                         @JsonProperty(required = true) double maxAccelMeters,
                                         @JsonProperty(required = true) List<Pose2d> waypoints){
        constraint = new DifferentialDriveVoltageConstraint(
                drivetrain.getLeftFeedforwardCalculator(),
                drivetrain.getDriveKinematics(),
                12);

        // Create config for trajectory
        configuration = new TrajectoryConfig(maxSpeedMeters, maxAccelMeters)
                .setKinematics(drivetrain.getDriveKinematics())
                .addConstraint(constraint);

        this.waypoints = waypoints;
    }

    public Trajectory getTrajectory(Pose2d startingPose){
        waypoints.add(0, startingPose);
        trajectory = TrajectoryGenerator.generateTrajectory(waypoints, configuration);
        return trajectory;
    }
}
