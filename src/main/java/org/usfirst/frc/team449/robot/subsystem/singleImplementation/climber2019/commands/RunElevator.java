package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunElevator extends Command {

    private final MotionProfile backProfile, frontProfile;

    protected enum MoveType {BACK, FRONT, BOTH}

    private final MoveType moveType;

    private final SubsystemClimber2019 subsystem;

    private final Double unstickTolerance;

    private final double initBackPos, initFrontPos, offset;

    private boolean doneUnsticking;

    @JsonCreator
    public RunElevator(MoveType moveType, double maxVel, double maxAccel, double startPos, double endPos, double offset,
                       Double unstickTolerance, SubsystemClimber2019 subsystem) {
        requires(subsystem);
        this.moveType = moveType;
        this.subsystem = subsystem;
        this.offset = offset;
        this.unstickTolerance = unstickTolerance;

        initBackPos = subsystem.getBackPos();
        initFrontPos = subsystem.getFrontPos();

        MotionProfileConstraints constraints = new MotionProfileConstraints(maxVel, maxAccel);

        backProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(endPos),
                new MotionState(0,startPos,0,0));
        frontProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(endPos + offset),
                new MotionState(0,startPos,0,0));
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        Logger.addEvent("RunElevator initialize, " + moveType, this.getClass());
        doneUnsticking = unstickTolerance == null;
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        if (!doneUnsticking) {
            MotionState motionState = new MotionState(0, 10, 0, 0);
            switch (moveType) {
                case BACK:
                    doneUnsticking = subsystem.profileBackUntilMovement(motionState, initBackPos, unstickTolerance);
                    break;
                case FRONT:
                    doneUnsticking = subsystem.profileFrontUntilMovement(motionState, initFrontPos, unstickTolerance);
                    break;
                case BOTH:
                    doneUnsticking = subsystem.profileBackUntilMovement(motionState, initBackPos, unstickTolerance)
                                  && subsystem.profileFrontUntilMovement(motionState, initFrontPos, unstickTolerance);
                    break;
                default:
                    break;
            }
        }
        if (!doneUnsticking) {
            return;
        }

        double t = timeSinceInitialized();
        switch (moveType) {
            case BACK:
                subsystem.profileBack(backProfile.stateByTimeClamped(t));
                break;
            case FRONT:
                subsystem.profileFront(frontProfile.stateByTimeClamped(t));
                break;
            case BOTH:
                subsystem.profileBack(backProfile.stateByTimeClamped(t));
                subsystem.profileFront(frontProfile.stateByTimeClamped(t));
                break;
            default:
                break;
        }
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends, like shutting off a
     * motor that was being used in the command.
     */
    @Override
    protected void end() {
        Logger.addEvent("RunElevator end, " + timeSinceInitialized(), this.getClass());
        switch (moveType) {
            case BACK:
                subsystem.fullStopBack();
                break;
            case FRONT:
                subsystem.fullStopFront();
                break;
            case BOTH:
                subsystem.fullStopBack();
                subsystem.fullStopFront();
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean isFinished() {
        double t = timeSinceInitialized();
        return backProfile.stateByTimeClamped(t).coincident(backProfile.endState())
            && frontProfile.stateByTimeClamped(t).coincident(frontProfile.endState());
    }
}
