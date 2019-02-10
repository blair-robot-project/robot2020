package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

public class AbortClimb extends CommandGroup {

	@JsonCreator
	public AbortClimb(SubsystemClimber2019 subsystem) {
		requires(subsystem);
		subsystem.fullStopBack();
		subsystem.fullStopFront();
		subsystem.fullStopDrive();
	}

}
