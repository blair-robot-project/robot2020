package org.usfirst.frc.team449.robot.drive.unidirectional.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.other.Logger;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunDriveMP<T extends DriveUnidirectionalWithGyro> extends Command {

	private final MotionProfile leftProfile, rightProfile;

	private final T subsystem;

	@JsonCreator
	public RunDriveMP(double maxVel, double maxAccel, double distance,
	                      T subsystem) {
		requires(subsystem);
		this.subsystem = subsystem;

		MotionProfileConstraints constraints = new MotionProfileConstraints(maxVel, maxAccel);

		double initPosLeft = subsystem.getLeftPosCached(), initPosRight = subsystem.getRightPosCached();

		leftProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(initPosLeft + distance),
				new MotionState(0, initPosLeft, 0, 0));
		rightProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(initPosRight + distance),
				new MotionState(0, initPosRight, 0, 0));
	}

	/**
	 * The initialize method is called the first time this Command is run after being started.
	 */
	@Override
	protected void initialize() {
		Logger.addEvent("RunDriveMP initialize, ", this.getClass());
	}

	/**
	 * The execute method is called repeatedly until this Command either finishes or is canceled.
	 */
	@Override
	protected void execute() {
		double t = timeSinceInitialized();
		subsystem.profileLeft(leftProfile.stateByTimeClamped(t));
		subsystem.profileRight(rightProfile.stateByTimeClamped(t));
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