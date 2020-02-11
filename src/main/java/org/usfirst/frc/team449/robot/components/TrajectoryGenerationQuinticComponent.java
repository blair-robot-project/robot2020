package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedPose2d;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public class TrajectoryGenerationQuinticComponent implements TrajectoryGenerationComponent{

    TrajectoryConstraint constraint;
    TrajectoryConfig configuration;
    List<Pose2d> waypoints = new ArrayList<>();
    Trajectory trajectory;

    @JsonCreator
    public TrajectoryGenerationQuinticComponent(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                                @JsonProperty(required = true) double maxSpeedMeters,
                                                @JsonProperty(required = true) double maxAccelMeters,
                                                @JsonProperty(required = true) List<MappedPose2d> waypoints,
                                                @Nullable Boolean reversed){
        constraint = new DifferentialDriveVoltageConstraint(
                drivetrain.getLeftFeedforwardCalculator(),
                drivetrain.getDriveKinematics(),
                12);

        // Create config for trajectory
        configuration = new TrajectoryConfig(maxSpeedMeters, maxAccelMeters)
                .setKinematics(drivetrain.getDriveKinematics())
                .addConstraint(constraint)
                .setReversed(reversed != null ? reversed : false);

        for(var entry : waypoints){
            this.waypoints.add(entry.pose);
        }
    }

    @Override
    public Trajectory getTrajectory(){
        trajectory = TrajectoryGenerator.generateTrajectory(waypoints, configuration);
        return trajectory;
    }
}
