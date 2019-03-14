package org.usfirst.frc.team449.robot.drive.unidirectional.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.command.Command;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;


/**
 * Directly run a 1D 254 motion profile on the robot drive.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunDriveMP<T extends DriveUnidirectionalWithGyro> extends Command {

	/**
	 * The profiles for the left and right side of the drive to follow, respectively.
	 */
	@NotNull
	private final MotionProfile leftProfile, rightProfile;

	/**
	 * The robot drive.
	 */
	@NotNull
	private final T subsystem;

	/**
	 * The initial positions of the left and right side of the drive, respectively.
	 */
	private double initPosLeft, initPosRight;

	/**
	 * Default constructor
	 *
	 * @param maxVel    The maximum velocity of the profile, in feet/second.
	 * @param maxAccel  The maximum acceleration of the profile, in feet/second^2.
	 * @param distance  The distance to travel, in feet.
	 * @param subsystem The robot drive.
	 */
	@JsonCreator
	public RunDriveMP(@JsonProperty(required = true) double maxVel,
	                  @JsonProperty(required = true) double maxAccel,
	                  @JsonProperty(required = true) double distance,
	                  @JsonProperty(required = true) @NotNull T subsystem) {
		requires(subsystem);
		this.subsystem = subsystem;

		MotionProfileConstraints constraints = new MotionProfileConstraints(maxVel, maxAccel);

		leftProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(distance),
				new MotionState(0, 0, 0, 0));
		rightProfile = MotionProfileGenerator.generateProfile(constraints, new MotionProfileGoal(distance),
				new MotionState(0, 0, 0, 0));
	}

	/**
	 * Store the initial position of the each side of the drive.
	 */
	@Override
	protected void initialize() {
		Logger.addEvent("RunDriveMP initialize, ", this.getClass());
		initPosLeft = subsystem.getLeftPosCached();
		initPosRight = subsystem.getRightPosCached();
	}

	/**
	 * Command the profile state for time t, offset by the initial position.
	 */
	@Override
	protected void execute() {
		double t = timeSinceInitialized();
		subsystem.profileLeftWithOffset(leftProfile.stateByTimeClamped(t), initPosLeft);
		subsystem.profileRightWithOffset(rightProfile.stateByTimeClamped(t), initPosRight);
	}

	/**
	 * Stop the drive.
	 */
	@Override
	protected void end() {
		Logger.addEvent("RunDriveMP end, " + timeSinceInitialized(), this.getClass());
		subsystem.fullStop();
	}

	/**
	 * Run until the current state of each profile coincides with the end state of each profile.
	 *
	 * @return true if the profiles have finished, false otherwise.
	 */
	@Override
	protected boolean isFinished() {
		double t = timeSinceInitialized();
		return leftProfile.stateByTimeClamped(t).coincident(leftProfile.endState())
			&& rightProfile.stateByTimeClamped(t).coincident(rightProfile.endState());
	}

}