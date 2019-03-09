package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

/**
 * Drive the leg-drive forward a given distance.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DriveLegWheels extends Command {

    /**
     * The profile for the leg-drive to follow.
     */
    @NotNull
    private final MotionProfile profile;

    /**
     * The climber subsystem.
     */
    @NotNull
    private final SubsystemClimber2019 climber;

    /**
     * The initial position of the leg-drive motor.
     */
    private double initPos;

    /**
     * Default constructor.
     *
     * @param maxVel    The maximum velocity for driving the leg-drive, in feet/second.
     * @param maxAccel  The maximum acceleration for driving the leg-drive, in feet/second^2.
     * @param distance  The distance to drive the leg-drive, in feet.
     * @param climber   The climber subsystem.
     */
    @JsonCreator
    public DriveLegWheels(@JsonProperty(required = true) double maxVel,
                          @JsonProperty(required = true) double maxAccel,
                          @JsonProperty(required = true) double distance,
                          @JsonProperty(required = true) @NotNull SubsystemClimber2019 climber) {
        requires(climber);
        this.climber = climber;

        MotionProfileConstraints constraints = new MotionProfileConstraints(maxVel, maxAccel);

        profile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(distance),
                new MotionState(0, 0, 0, 0));
    }

    /**
     * Store the initial position of the leg-drive.
     */
    @Override
    protected void initialize() {
        Logger.addEvent("DriveLegWheels initialize, ", this.getClass());
        initPos = climber.getDrivePos();
    }

    /**
     * Command the profile state for time t, offset by the initial position.
     */
    @Override
    protected void execute() {
        double t = timeSinceInitialized();
        climber.profileDriveWithOffset(profile.stateByTimeClamped(t), initPos);
    }

    /**
     * Stop the leg-drive.
     */
    @Override
    protected void end() {
        Logger.addEvent("DriveLegWheels end, " + timeSinceInitialized(), this.getClass());
//        climber.fullStopDrive();
    }

    /**
     * Run until the current state of the profile coincides with the end state of the profile.
     *
     * @return true if the profile has finished, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        double t = timeSinceInitialized();
        return profile.stateByTimeClamped(t).coincident(profile.endState());
    }
}
