package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.commands.general.RequireSubsystem;
import org.usfirst.frc.team449.robot.drive.DriveSubsystem;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

/**
 * Interrupt all commands controlling the climb subsystem or the robot drive, then stop all motors.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class AbortClimb<T extends Subsystem & DriveSubsystem> extends CommandGroup {

	/**
	 * Default constructor
	 *
	 * @param climber The climber subsystem.
	 * @param drive   The drive of the robot.
	 */
	@JsonCreator
	public AbortClimb(@JsonProperty(required = true) @NotNull SubsystemClimber2019 climber,
	                  @JsonProperty(required = true) @NotNull T drive) {
		RequireSubsystem requireClimber = new RequireSubsystem(climber);
		RequireSubsystem requireDrive = new RequireSubsystem(drive);
		StopClimb stopClimb = new StopClimb<>(climber, drive);

		addParallel(requireClimber);
		addSequential(requireDrive);
		addSequential(stopClimb);
	}

}
