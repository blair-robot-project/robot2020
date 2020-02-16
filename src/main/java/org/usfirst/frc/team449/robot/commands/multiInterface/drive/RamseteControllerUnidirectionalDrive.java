package org.usfirst.frc.team449.robot.commands.multiInterface.drive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

import io.github.oblarg.oblog.Loggable;

import org.usfirst.frc.team449.robot.components.TrajectoryGenerationComponent;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedPIDController;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RamseteControllerUnidirectionalDrive extends RamseteCommand implements Loggable {

    private DriveUnidirectionalWithGyro driveTrain;
    private NetworkTable falconDashboard;
    private MappedPIDController leftPidController;
    private MappedPIDController rightPidController;

    @JsonCreator
    public RamseteControllerUnidirectionalDrive(@JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
                                                @JsonProperty(required = true) MappedPIDController leftPidController,
                                                @JsonProperty(required = true) MappedPIDController rightPidController,
                                                @JsonProperty(required = true) TrajectoryGenerationComponent trajectoryGenerator,
                                                boolean robotRelative){
        super(trajectoryGenerator.getTrajectory(),
                drivetrain::getCurrentPose,
                new RamseteController(),
                drivetrain.getLeftFeedforwardCalculator(),
                drivetrain.getDriveKinematics(),
                drivetrain::getWheelSpeeds,
                leftPidController,
                rightPidController,
                drivetrain::setVoltage);

        this.driveTrain = drivetrain;
        this.leftPidController = leftPidController;
        this.rightPidController = rightPidController;
        addRequirements(driveTrain);

        // TODO don't reset odometry; transform the trajectory
        if (robotRelative) {
            drivetrain.resetOdometry(trajectoryGenerator.getTrajectory().getInitialPose());
        }

        falconDashboard = NetworkTableInstance.getDefault().getTable("Live_Dashboard");
        //todo hook into timer to get expected pose at each
        falconDashboard.getEntry("isFollowingPath").setBoolean(true);
//        falconDashboard.getEntry("pathX").setDouble(trajectoryGenerator.getTrajectory().sample(0).poseMeters.getTranslation().getX());
    }

    @Override
    public void execute(){
        super.execute();

        //update falcondashboard
        falconDashboard.getEntry("robotX").setDouble(Units.metersToFeet(driveTrain.getCurrentPose().getTranslation().getX()));
        falconDashboard.getEntry("robotY").setDouble(Units.metersToFeet(driveTrain.getCurrentPose().getTranslation().getY()));
        falconDashboard.getEntry("robotHeading").setDouble(driveTrain.getCurrentPose().getRotation().getRadians());
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        falconDashboard.getEntry("isFollowingPath").setBoolean(false);
        driveTrain.fullStop();
    }
}
