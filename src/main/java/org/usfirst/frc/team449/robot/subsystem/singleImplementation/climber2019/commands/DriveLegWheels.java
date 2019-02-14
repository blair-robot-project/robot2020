package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DriveLegWheels extends Command {

    private final MotionProfile profile;

    private final SubsystemClimber2019 subsystem;

    @JsonCreator
    public DriveLegWheels(double maxVel, double maxAccel, double startPos, double endPos,
                          SubsystemClimber2019 subsystem) {
        requires(subsystem);
        this.subsystem = subsystem;

        MotionProfileConstraints constraints = new MotionProfileConstraints(maxVel, maxAccel);

        profile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(endPos),
                new MotionState(0,startPos,0,0));
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        Logger.addEvent("DriveLegWheels initialize, ", this.getClass());
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        double t = timeSinceInitialized();
        subsystem.profileDrive(profile.stateByTimeClamped(t));
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends, like shutting off a
     * motor that was being used in the command.
     */
    @Override
    protected void end() {
        Logger.addEvent("DriveLegWheels end, " + timeSinceInitialized(), this.getClass());
        subsystem.fullStopDrive();
    }

    @Override
    protected boolean isFinished() {
        double t = timeSinceInitialized();
        return profile.stateByTimeClamped(t).coincident(profile.endState());
    }
}
