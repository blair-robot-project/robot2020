package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

/**
 * Run one or both elevators for a certain distance.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunElevator extends Command {

    /**
     * The profiles for the back and front elevators, respectively.
     */
    private final MotionProfile backProfile, frontProfile;

    /**
     * An enum to select whether we want to move just the back elevator, just the front, or both.
     */
    protected enum MoveType {BACK, FRONT, BOTH}

    /**
     * Whether we want to move just the back elevator, just the front, or both.
     */
    private final MoveType moveType;

    /**
     * The climber subsystem.
     */
    private final SubsystemClimber2019 climber;

    /**
     * How far to make the elevator extend before retracting, to unstick the brake, in feet.
     */
    private final Double unstickTolerance;

    /**
     * The initial positions of the front and back elevators.
     */
    private double initBackPos, initFrontPos;

    /**
     * Whether we are done unsticking the brake.
     */
    private boolean doneUnsticking;

    @Nullable
    private Double startPos;

    /**
     * Default constructor.
     *
     * @param moveType         Whether we want to move just the back elevator, just the front, or both.
     * @param maxVel           The maximum velocity to run the elevators at, in feet/second.
     * @param maxAccel         The maximum velocity to run the elevators at, in feet/second^2.
     * @param startPos         The start position of the elevator, in feet.
     * @param endPos           The end position of the elevator, in feet.
     * @param offset           How much the front elevator should extend further than the back elevator, in feet.
     * @param velReduction     How much to reduce the max velocity of the back elevator profile, as a percent.
     * @param accelReduction   How much to reduce the max acceleration of the back elevator profile, as a percent.
     * @param unstickTolerance How far to make the elevator extend before retracting, to unstick the brake, in feet.
     * @param climber          The climber subsystem.
     */
    @JsonCreator
    public RunElevator(@JsonProperty(required = true) @NotNull MoveType moveType,
                       @JsonProperty(required = true) double maxVel,
                       @JsonProperty(required = true) double maxAccel,
                       @Nullable Double startPos,
                       @JsonProperty(required = true) double endPos,
                       double offset,
                       double velReduction,
                       double accelReduction,
                       @Nullable Double unstickTolerance,
                       @JsonProperty(required = true) @NotNull SubsystemClimber2019 climber) {
        requires(climber);
        this.moveType = moveType;
        this.climber = climber;
        this.unstickTolerance = unstickTolerance;
        this.startPos = startPos;

        MotionProfileConstraints backConstraints = new MotionProfileConstraints((1 - velReduction) * maxVel,
                                                                                (1 - accelReduction) * maxAccel);
        MotionProfileConstraints frontConstraints = new MotionProfileConstraints(maxVel, maxAccel);

        if (startPos == null) {
            backProfile = MotionProfileGenerator.generateProfile(backConstraints, new MotionProfileGoal(endPos),
                    new MotionState(0,0,0,0));
            frontProfile = MotionProfileGenerator.generateProfile(frontConstraints, new MotionProfileGoal(endPos + offset),
                    new MotionState(0,0,0,0));
        } else {
            backProfile = MotionProfileGenerator.generateProfile(backConstraints, new MotionProfileGoal(endPos),
                    new MotionState(0, startPos, 0, 0));
            frontProfile = MotionProfileGenerator.generateProfile(frontConstraints, new MotionProfileGoal(endPos + offset),
                    new MotionState(0, startPos, 0, 0));
        }
    }

    /**
     * Determine whether we need to unstick based on whether unstickTolerance is null.
     */
    @Override
    protected void initialize() {
        Logger.addEvent("RunElevator initialize, " + moveType, this.getClass());
        doneUnsticking = unstickTolerance == null;
        initBackPos = climber.getBackPos();
        initFrontPos = climber.getFrontPos();
        System.out.println("START: " + timeSinceInitialized());
    }

    /**
     * Unstick the brake, and run the elevator once that's done.
     */
    @Override
    protected void execute() {
        if (!doneUnsticking) {
            MotionState motionState = new MotionState(0, 10, 0, 0);
            switch (moveType) {
                case BACK:
                    doneUnsticking = climber.profileBackUntilMovement(motionState, initBackPos, unstickTolerance);
                    break;
                case FRONT:
                    doneUnsticking = climber.profileFrontUntilMovement(motionState, initFrontPos, unstickTolerance);
                    break;
                case BOTH:
                    doneUnsticking = climber.profileBackUntilMovement(motionState, initBackPos, unstickTolerance)
                                  && climber.profileFrontUntilMovement(motionState, initFrontPos, unstickTolerance);
                    break;
                default:
                    break;
            }
        }
        if (!doneUnsticking) {
            return;
        }

        double t = timeSinceInitialized();
        if (startPos == null) {
            switch (moveType) {
                case BACK:
                    climber.profileBackWithOffset(backProfile.stateByTimeClamped(t), initBackPos);
                    break;
                case FRONT:
                    climber.profileFrontWithOffset(frontProfile.stateByTimeClamped(t), initFrontPos);
                    break;
                case BOTH:
                    climber.profileBackWithOffset(backProfile.stateByTimeClamped(t), initBackPos);
                    climber.profileFrontWithOffset(frontProfile.stateByTimeClamped(t), initFrontPos);
                    break;
                default:
                    break;
            }
        } else {
            switch (moveType) {
                case BACK:
                    climber.profileBack(backProfile.stateByTimeClamped(t));
                    break;
                case FRONT:
                    climber.profileFront(frontProfile.stateByTimeClamped(t));
                    break;
                case BOTH:
                    climber.profileBack(backProfile.stateByTimeClamped(t));
                    climber.profileFront(frontProfile.stateByTimeClamped(t));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Stop the elevator motors being used.
     */
    @Override
    protected void end() {
        Logger.addEvent("RunElevator end, " + timeSinceInitialized(), this.getClass());
        System.out.println("END: " + timeSinceInitialized());
        switch (moveType) {
            case BACK:
                climber.fullStopBack();
                break;
            case FRONT:
                climber.fullStopFront();
                break;
            case BOTH:
                climber.fullStopBack();
                climber.fullStopFront();
                break;
            default:
                break;
        }
    }

    /**
     * Run until the current state of each profile coincides with the end state of each profile.
     *
     * @return true if the profiles have finished, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        double t = timeSinceInitialized();
        return backProfile.stateByTimeClamped(t).coincident(backProfile.endState())
            && frontProfile.stateByTimeClamped(t).coincident(frontProfile.endState());
    }
}
