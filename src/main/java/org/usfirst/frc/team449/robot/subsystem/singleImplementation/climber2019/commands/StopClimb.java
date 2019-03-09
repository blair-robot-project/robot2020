package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.drive.DriveSubsystem;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

/**
 * Turn off all motors in the climb subsystem and drive.
 */
public class StopClimb<T extends Subsystem & DriveSubsystem> extends InstantCommand {

	/**
	 * The climber subsystem.
	 */
	@NotNull
	private SubsystemClimber2019 climber;

	/**
	 * The drive of the robot.
	 */
	@NotNull
	private T drive;

	/**
	 * Default constructor
	 *
	 * @param climber The climber subsystem.
	 * @param drive   The drive of the robot.
	 */
	public StopClimb(@NotNull SubsystemClimber2019 climber,
	                 @NotNull T drive) {
		this.climber = climber;
		this.drive = drive;
	}

	/**
	 * Log on initialize.
	 */
	@Override
	protected void initialize() {
		Logger.addEvent("StopClimb init", this.getClass());
	}

	/**
	 * Stop all motors
	 */
	@Override
	protected void execute() {
		climber.fullStopBack();
		climber.fullStopFront();
		climber.fullStopDrive();
		drive.fullStop();
	}

	/**
	 * Log on end.
	 */
	public void end() {
		Logger.addEvent("StopClimb end", this.getClass());
	}

}
