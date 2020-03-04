package org.usfirst.frc.team449.robot.drive.unidirectional.commands.motionprofiling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import io.github.oblarg.oblog.Loggable;
import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team449.robot.auto.commands.AutonomousCommand;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedPIDController;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public class RamseteControllerGoToPosition extends CommandBase implements Loggable, AutonomousCommand {

  private DriveUnidirectionalWithGyro drivetrain;
  private MappedPIDController leftPidController;
  private MappedPIDController rightPidController;
  private Pose2d endingPose;
  private List<Translation2d> translations;
  private RamseteCommand wrappedCommand;
  private TrajectoryConfig config;
  private Trajectory trajectory;

  @JsonCreator
  public RamseteControllerGoToPosition(
      @JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
      @JsonProperty(required = true) final double maxSpeedMeters,
      @JsonProperty(required = true) final double maxAccelMeters,
      Double maxCentripetalAcceleration,
      @JsonProperty(required = true) MappedPIDController leftPidController,
      @JsonProperty(required = true) MappedPIDController rightPidController,
      @JsonProperty(required = true) Pose2d endingPose,
      List<Translation2d> translations,
      boolean reversed) {
    this.drivetrain = drivetrain;
    this.leftPidController = leftPidController;
    this.rightPidController = rightPidController;
    this.endingPose = endingPose;
    this.translations = translations;


    // Create config for trajectory
    config =
        new TrajectoryConfig(maxSpeedMeters, maxAccelMeters)
            .setKinematics(drivetrain.getDriveKinematics())
            .addConstraint(drivetrain.getVoltageConstraint())
            .setReversed(reversed);

    trajectory = TrajectoryGenerator.generateTrajectory(
            drivetrain.getCurrentPose(),
            translations == null ? new ArrayList<>() : translations,
            endingPose,
            config);

    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    trajectory = TrajectoryGenerator.generateTrajectory(
            drivetrain.getCurrentPose(),
            translations == null ? new ArrayList<>() : translations,
            endingPose,
            config);
    wrappedCommand =
        new RamseteCommand(trajectory,
            drivetrain::getCurrentPose,
            new RamseteController(),
            drivetrain.getLeftFeedforwardCalculator(),
            drivetrain.getDriveKinematics(),
            drivetrain::getWheelSpeeds,
            leftPidController,
            rightPidController,
            drivetrain::setVoltage);
    wrappedCommand.initialize();
  }

  @Override
  public void execute() {
    wrappedCommand.execute();
  }

  @Override
  public void end(boolean interrupted) {
    wrappedCommand.end(interrupted);
  }

  @Override
  public boolean isFinished() {
    return wrappedCommand.isFinished();
  }

  @Override
  public Double getRunTimeSeconds() {
    return trajectory.getTotalTimeSeconds();
  }
}
