package org.usfirst.frc.team449.robot.commands.multiInterface.drive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.usfirst.frc.team449.robot.components.TrajectoryGenerationComponent;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RamseteControllerUnidirectionalDrive extends CommandBase {

    DriveUnidirectionalWithGyro drivetrain;
    RamseteController ramseteFeedback;
    PIDController leftController, rightController;
    DifferentialDriveWheelSpeeds previousSpeeds;
    Trajectory trajectory;

    double absoluteTime;
    double relativeTime;

    @JsonCreator
    public RamseteControllerUnidirectionalDrive(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                                @JsonProperty(required = true) double P,
                                                @JsonProperty(required = true) double D,
                                                @JsonProperty(required = true) TrajectoryGenerationComponent trajectoryGenerator){
        this.drivetrain = drivetrain;
        ramseteFeedback = new RamseteController();
        leftController = new PIDController(P, 0, D);
        rightController = new PIDController(P, 0, D);

        trajectory = trajectoryGenerator.getTrajectory();

        addRequirements(drivetrain);
    }

    @Override
    public void initialize(){
        absoluteTime = Timer.getFPGATimestamp();
        Trajectory.State initialState = trajectory.sample(Timer.getFPGATimestamp() - absoluteTime);
        previousSpeeds = drivetrain.getDriveKinematics().toWheelSpeeds(new ChassisSpeeds(initialState.velocityMetersPerSecond,
                0,
                (initialState.curvatureRadPerMeter * initialState.velocityMetersPerSecond)));
        leftController.reset();
        rightController.reset();
    }

    @Override
    public void execute(){
        relativeTime = Timer.getFPGATimestamp();
        DifferentialDriveWheelSpeeds targetWheelSpeeds = drivetrain.getDriveKinematics().toWheelSpeeds(
                ramseteFeedback.calculate(drivetrain.getCurrentPose(), trajectory.sample(Timer.getFPGATimestamp() - absoluteTime)));
        DifferentialDriveWheelSpeeds currentWheelSpeeds = drivetrain.getWheelSpeeds();

        double leftTarget = targetWheelSpeeds.leftMetersPerSecond;
        double rightTarget = targetWheelSpeeds.rightMetersPerSecond;
        double leftCurrent = currentWheelSpeeds.leftMetersPerSecond;
        double rightCurrent = currentWheelSpeeds.leftMetersPerSecond;

        double leftFeedforward = drivetrain.getLeftFeedforwardCalculator().calculate(leftTarget,
                (leftTarget - previousSpeeds.leftMetersPerSecond) / (Timer.getFPGATimestamp() - relativeTime));
        double rightFeedforward = drivetrain.getRightFeedforwardCalculator().calculate(leftTarget,
                (leftTarget - previousSpeeds.leftMetersPerSecond) / (Timer.getFPGATimestamp() - relativeTime));

        double leftOutput = leftFeedforward + leftController.calculate(leftCurrent, leftTarget);
        double rightOutput = rightFeedforward + rightController.calculate(rightCurrent, rightTarget);

        drivetrain.setVoltage(leftOutput, rightOutput);
    }

    @Override
    public boolean isFinished() {
        return (Timer.getFPGATimestamp() - absoluteTime) >= trajectory.getTotalTimeSeconds();
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
