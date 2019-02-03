package org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.TwoSideMPSubsystem.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.components.PathRequester;
import org.usfirst.frc.team449.robot.other.Waypoint;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.TwoSideMPSubsystem.SubsystemMPTwoSides;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.commands.GetPathFromJetson;

/**
 * A command that drives the given subsystem to x=0,z=0,yaw=0.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class GoToVisionTarget<T extends Subsystem & SubsystemMPTwoSides & SubsystemAHRS> extends CommandGroup {

    /**
     * The subsystem to run the path gotten from the Jetson on.
     */
    @NotNull
    private final T subsystem;
    /**
     * Network table for pulling info from the Jetson
     */
    private final NetworkTable table;

    /**
     * Default constructor
     *
     * @param subsystem     The subsystem to run the path gotten from the Jetson on.
     * @param pathRequester The object for interacting with the Jetson.
     * @param maxVel        The maximum velocity, in feet/second.
     * @param maxAccel      The maximum acceleration, in feet/(second^2)
     * @param maxJerk       The maximum jerk, in feet/(second^3)
     * @param deltaTime     The time between setpoints in the profile, in seconds.
     */
    @JsonCreator
    public GoToVisionTarget(@NotNull @JsonProperty(required = true) T subsystem,
                            @NotNull @JsonProperty(required = true) PathRequester pathRequester,
                            @JsonProperty(required = true) double maxVel,
                            @JsonProperty(required = true) double maxAccel,
                            @JsonProperty(required = true) double maxJerk,
                            @JsonProperty(required = true) double deltaTime) {
        this.subsystem = subsystem;
        this.table = NetworkTableInstance.getDefault().getTable("SmartDashboard").getSubTable("jetson-vision");
        GetPathFromJetson getPath = new GetPathFromJetson(pathRequester, null, deltaTime, maxVel, maxAccel, maxJerk,
                false);
        GoToPositionRelative goToPositionRelative = new GoToPositionRelative<>(getPath, subsystem);
        goToPositionRelative.setWaypoints(this::getWaypoints);
        addSequential(goToPositionRelative);
    }

    /**
     * @return The points for the path to hit, relative to the robot's current position.
     */
    @NotNull
    private Waypoint[] getWaypoints() {
        System.out.println("X: " + getX());
        System.out.println("Y: " + getY());
        System.out.println("THETA: " + getTheta());
        Waypoint[] toRet = new Waypoint[1];
//        toRet[0] = new Waypoint(0,0,0);
        toRet[0] = new Waypoint(getX(), getY(), getTheta());
        return toRet;
    }

    /**
     * @return The relative X distance to the setpoint, in feet.
     */
    private double getX() {
        //Not a typo, this is how our coordinate transform works
        return -table.getEntry("z").getDouble(0);
    }

    /**
     * @return The relative Y distance to the setpoint, in feet.
     */
    private double getY() {
        //Not a typo, this is how our coordinate transform works
        return -table.getEntry("x").getDouble(0);
    }

    /**
     * @return The relative angular distance to the setpoint, in degrees.
     */
    private double getTheta() {
        return -Math.toDegrees(table.getEntry("yaw").getDouble(0));
    }
}
