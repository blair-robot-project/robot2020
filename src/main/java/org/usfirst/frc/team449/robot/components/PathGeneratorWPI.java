package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import org.usfirst.frc.team449.robot.other.MotionProfileData;
import org.usfirst.frc.team449.robot.other.Waypoint;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for WPILib's onboard path generation.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class PathGeneratorWPI {

    /**
     * The amount of time between each point, in seconds.
     */
    private final double deltaTime;

    /**
     * The maximum velocity, in feet/second.
     */
    private final double maxVel;

    /**
     * The maximum acceleration, in feet/(second^2)
     */
    private final double maxAccel;

    /**
     * Whether to reset the encoder position before running the profile.
     */
    private final boolean resetPosition;

    /**
     * The list of positions, velocities, and accelerations in the profile. Field to avoid garbage collection.
     */
    private List<Double> pos, vel, accel;

    /**
     * Default constructor.
     *
     * @param deltaTime     The time between setpoints in the profile, in seconds.
     * @param maxVel        The maximum velocity, in feet/second.
     * @param maxAccel      The maximum acceleration, in feet/(second^2)
     * @param resetPosition Whether or not to reset the encoder position before running the profile.
     */
    @JsonCreator
    public PathGeneratorWPI(double deltaTime, double maxVel, double maxAccel, boolean resetPosition) {
        this.deltaTime = deltaTime;
        this.maxVel = maxVel;
        this.maxAccel = maxAccel;
        this.resetPosition = resetPosition;
    }

    /**
     * Generate a profile given the current state and desired end state.
     *
     * @param waypoints     The points for the path to hit.
     * @param inverted      Whether or not to invert the profiles.
     * @return A motion profile that will move from the current state to the destination.
     */
    public MotionProfileData[] generateProfile(Waypoint[] waypoints, boolean inverted) {
        List<Pose2d> pose2dList = new ArrayList<>();
        for (Waypoint waypoint : waypoints) {
            pose2dList.add(new Pose2d(waypoint.getX(), waypoint.getY(), new Rotation2d(waypoint.getThetaRadians())));
        }

        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(pose2dList, new TrajectoryConfig(maxVel, maxAccel));

        pos = new ArrayList<>();
        vel = new ArrayList<>();
        accel = new ArrayList<>();

        File pathOutput = new File("/home/lvuser/test/pathOutputWPI.txt");
        PrintWriter pathOutputWriter = null;
        try {
            pathOutput.createNewFile();
            pathOutputWriter = new PrintWriter(pathOutput);
            pathOutputWriter.flush();
        } catch (IOException e) {}

        for (double t = 0; t < trajectory.getTotalTimeSeconds(); t += deltaTime) {
            Trajectory.State state = trajectory.sample(t);
            System.out.println(state);
            pathOutputWriter.println(state);
            pos.add(state.poseMeters.getTranslation().getDistance(new Translation2d(0, 0)));
            vel.add(state.velocityMetersPerSecond);
            accel.add(state.accelerationMetersPerSecondSq);
        }

        pathOutputWriter.close();

        MotionProfileData data = new MotionProfileData(pos, vel, accel, deltaTime, inverted, false, resetPosition);
        return new MotionProfileData[] {data};
    }
}
