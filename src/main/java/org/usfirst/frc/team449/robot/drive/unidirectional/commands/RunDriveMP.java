package org.usfirst.frc.team449.robot.drive.unidirectional.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.other.Logger;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunDriveMP<T extends DriveUnidirectionalWithGyro> extends Command {

	private final MotionProfile leftProfile, rightProfile;

	private final T subsystem;

	private double initPosLeft, initPosRight;

	@JsonCreator
	public RunDriveMP(double maxVel, double maxAccel, double distance,
	                      @NotNull T subsystem) {
		requires(subsystem);
		this.subsystem = subsystem;

		MotionProfileConstraints constraints = new MotionProfileConstraints(maxVel, maxAccel);

		leftProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(distance),
				new MotionState(0, 0, 0, 0));
		rightProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(distance),
				new MotionState(0, 0, 0, 0));
	}

	/**
	 * The initialize method is called the first time this Command is run after being started.
	 */
	@Override
	protected void initialize() {
		Logger.addEvent("RunDriveMP initialize, ", this.getClass());
		initPosLeft = subsystem.getLeftPosCached();
		initPosRight = subsystem.getRightPosCached();
	}

	/**
	 * The execute method is called repeatedly until this Command either finishes or is canceled.
	 */
	@Override
	protected void execute() {
		double t = timeSinceInitialized();
		subsystem.profileLeftWithOffset(leftProfile.stateByTimeClamped(t), initPosLeft);
		subsystem.profileRightWithOffset(rightProfile.stateByTimeClamped(t), initPosRight);
	}

	/**
	 * Called when the command ended peacefully. This is where you may want to wrap up loose ends, like shutting off a
	 * motor that was being used in the command.
	 */
	@Override
	protected void end() {
		Logger.addEvent("RunDriveMP end, " + timeSinceInitialized(), this.getClass());
		subsystem.fullStop();
	}

	@Override
	protected boolean isFinished() {
		double t = timeSinceInitialized();
		return leftProfile.stateByTimeClamped(t).coincident(leftProfile.endState())
			&& rightProfile.stateByTimeClamped(t).coincident(rightProfile.endState());
	}

}