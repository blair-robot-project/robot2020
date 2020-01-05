package org.usfirst.frc.team449.robot.commands.multiInterface.drive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.other.Heartbeat;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RamseteControllerUnidirectionalDrive extends CommandBase {

    DriveUnidirectionalWithGyro drivetrain;
    Trajectory trajectory;
    Heartbeat timer;
    RamseteController ramseteFeedback;
    PIDController leftController, rightController;
    DifferentialDriveWheelSpeeds previousSpeeds;

    public RamseteControllerUnidirectionalDrive(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                                @JsonProperty(required = true) double P,
                                                @JsonProperty(required = true) double D){
        this.drivetrain = drivetrain;
        timer = new Heartbeat();
        ramseteFeedback = new RamseteController();
        leftController = new PIDController(P, 0, D);
        rightController = new PIDController(P, 0, D);

        // Create a voltage constraint to ensure we don't accelerate too fast
        TrajectoryConstraint autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                drivetrain.getLeftFeedforwardCalculator(),
                drivetrain.getDriveKinematics(),
                12);

        // Create config for trajectory
        TrajectoryConfig config = new TrajectoryConfig(1.32948238154, 0.1131952403)
                .setKinematics(drivetrain.getDriveKinematics())
                .addConstraint(autoVoltageConstraint);

        // An example trajectory to follow.  All units in meters.
        trajectory = TrajectoryGenerator.generateTrajectory(
                new Pose2d(0, 0, new Rotation2d(0)),
                List.of(
                        new Translation2d(1, 1),
                        new Translation2d(2, -1)
                ),
                new Pose2d(3, 0, new Rotation2d(0)),
                config
        );
    }

    @Override
    public void initialize(){
        timer.start();
        Trajectory.State initialState = trajectory.sample(timer.getAbsolute());
        previousSpeeds = drivetrain.getDriveKinematics().toWheelSpeeds(new ChassisSpeeds(initialState.velocityMetersPerSecond,
                0,
                (initialState.curvatureRadPerMeter * initialState.velocityMetersPerSecond)));
        leftController.reset();
        rightController.reset();
    }

    @Override
    public void execute(){
        DifferentialDriveWheelSpeeds targetWheelSpeeds = drivetrain.getDriveKinematics().toWheelSpeeds(
                ramseteFeedback.calculate(drivetrain.getCurrentPose(), trajectory.sample(timer.getAbsolute())));
        DifferentialDriveWheelSpeeds currentWheelSpeeds = drivetrain.getWheelSpeeds();

        double leftTarget = targetWheelSpeeds.leftMetersPerSecond;
        double rightTarget = targetWheelSpeeds.rightMetersPerSecond;
        double leftCurrent = currentWheelSpeeds.leftMetersPerSecond;
        double rightCurrent = currentWheelSpeeds.leftMetersPerSecond;

        double leftFeedforward = drivetrain.getLeftFeedforwardCalculator().calculate(leftTarget,
                (leftTarget - previousSpeeds.leftMetersPerSecond) / timer.getDifference());
        double rightFeedforward = drivetrain.getRightFeedforwardCalculator().calculate(leftTarget,
                (leftTarget - previousSpeeds.leftMetersPerSecond) / timer.getDifference());

        double leftOutput = leftFeedforward + leftController.calculate(leftCurrent, leftTarget);
        double rightOutput = rightFeedforward + rightController.calculate(rightCurrent, rightTarget);

        drivetrain.setOutput(leftOutput, rightOutput);
    }

    @Override
    public boolean isFinished() {
        return timer.getAbsolute() >= trajectory.getTotalTimeSeconds();
    }

    @Override
    public void end(boolean interrupted){
        if(interrupted){
            Shuffleboard.addEventMarker("Ramsete controller interrupted! Stopping the robot.", this.getClass().getSimpleName(), EventImportance.kNormal);
        }
        drivetrain.fullStop();
        Shuffleboard.addEventMarker("Ramsete controller end.", this.getClass().getSimpleName(), EventImportance.kNormal);
    }
}
