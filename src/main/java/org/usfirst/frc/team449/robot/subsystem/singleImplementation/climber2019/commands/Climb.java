package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team449.robot.commands.multiInterface.RunMotorUntilConditionMet;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.oi.buttons.SimpleButton;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.IRWithButtonOverride;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

public class Climb extends CommandGroup {

	@JsonCreator
	public Climb(SubsystemClimber2019 subsystem,
	             double maxVelDrop, double maxAccelDrop, double maxVelRetract, double maxAccelRetract,
	             double maxVelNudge, double maxAccelNudge,
	             double extendDistance, double partialRetractionDistance,
	             double nudge1Distance, double nudge2Distance,
	             MappedDigitalInput infraredSensor1, MappedDigitalInput infraredSensor2,
	             SimpleButton continueButton) {
		requires(subsystem);

		IRWithButtonOverride failsafe1 = new IRWithButtonOverride(infraredSensor1, continueButton);
		IRWithButtonOverride failsafe2 = new IRWithButtonOverride(infraredSensor2, continueButton);

		RunElevator dropLegs = new RunElevator(RunElevator.MoveType.BOTH, maxVelDrop, maxAccelDrop,
				0, extendDistance, subsystem);
		DriveLegWheels nudgeForwardLegsDropped = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				0, nudge1Distance, subsystem);
		RunMotorUntilConditionMet crawlForwardLegsDropped = new RunMotorUntilConditionMet(subsystem, failsafe1);
		RunElevator retractFrontLeg = new RunElevator(RunElevator.MoveType.FRONT, maxVelRetract, maxAccelRetract,
				extendDistance, 0, subsystem);
		DriveLegWheels nudgeForwardFrontLegRetracted = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				nudge1Distance, nudge2Distance, subsystem);
		RunMotorUntilConditionMet crawlForwardFrontLegRetracted = new RunMotorUntilConditionMet(subsystem, failsafe2);
		RunElevator retractBackLegPartially = new RunElevator(RunElevator.MoveType.FRONT, maxVelRetract, maxAccelRetract,
				extendDistance, extendDistance - partialRetractionDistance, subsystem);

		addSequential(dropLegs);
		addSequential(nudgeForwardLegsDropped);
		addSequential(crawlForwardLegsDropped);
		addSequential(retractFrontLeg);
		addSequential(nudgeForwardFrontLegRetracted);
		addSequential(crawlForwardFrontLegRetracted);
		addSequential(retractBackLegPartially);
	}
}
