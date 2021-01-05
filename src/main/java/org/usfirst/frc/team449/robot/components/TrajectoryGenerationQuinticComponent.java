package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import java.util.ArrayList;
import java.util.List;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedPose2d;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public class TrajectoryGenerationQuinticComponent implements TrajectoryGenerationComponent {

  final TrajectoryConstraint constraint;
  final TrajectoryConfig configuration;
  final List<Pose2d> waypoints = new ArrayList<>();
  Trajectory trajectory;

  @JsonCreator
  public TrajectoryGenerationQuinticComponent(
      @JsonProperty(required = true) final DriveUnidirectionalWithGyro drivetrain,
      @JsonProperty(required = true) final double maxSpeedMeters,
      @JsonProperty(required = true) final double maxAccelMeters,
      @JsonProperty(required = true) final List<MappedPose2d> waypoints) {
    this.constraint =
        new DifferentialDriveVoltageConstraint(
            drivetrain.getLeftFeedforwardCalculator(), drivetrain.getDriveKinematics(), 12);

    // Create config for trajectory
    this.configuration =
        new TrajectoryConfig(maxSpeedMeters, maxAccelMeters)
            .setKinematics(drivetrain.getDriveKinematics())
            .addConstraint(this.constraint);

    for (final var entry : waypoints) {
      this.waypoints.add(entry.pose);
    }
  }

  @Override
  public Trajectory getTrajectory() {
    this.trajectory = TrajectoryGenerator.generateTrajectory(this.waypoints, this.configuration);
    return this.trajectory;
  }
}
