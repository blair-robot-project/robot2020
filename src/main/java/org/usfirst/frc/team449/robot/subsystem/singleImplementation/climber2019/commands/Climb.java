package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team449.robot.commands.multiInterface.RunMotorUntilConditionMet;
import org.usfirst.frc.team449.robot.commands.multiSubsystem.DriveStraightUntilConditionMet;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.drive.unidirectional.commands.RunDriveMP;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.IRWithButtonOverride;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

public class Climb extends CommandGroup {

	@JsonCreator
	public Climb(@JsonProperty(required = true) SubsystemClimber2019 subsystem,
	             DriveUnidirectionalWithGyro drive,
	             double maxVelDrop, double maxAccelDrop, double maxVelRetract, double maxAccelRetract,
	             double maxVelNudge, double maxAccelNudge,
	             double extendDistance, double partialRetractionDistance,
	             double nudge1Distance, double nudge2Distance,
	             IRWithButtonOverride failsafe1, IRWithButtonOverride failsafe2) {
		requires(subsystem);

		RunElevator dropLegs = new RunElevator(RunElevator.MoveType.BOTH, maxVelDrop, maxAccelDrop,
				0, extendDistance, subsystem);
		DriveLegWheels nudgeLegsForwardLegsDropped = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				0, nudge1Distance, subsystem);
//		RunDriveMP nudgeDriveForwardLegsDropped = new RunDriveMP(maxVelNudge, maxAccelNudge,
//				0, nudge1Distance, drive);
		RunMotorUntilConditionMet crawlLegsForwardLegsDropped = new RunMotorUntilConditionMet(subsystem, failsafe1);
//		DriveStraightUntilConditionMet crawlDriveForwardLegsDropped = new DriveStraightUntilConditionMet(
//				2, null, 0, null, null,
//				0, false, 0, 0, 0, drive, failsafe1, 0.1);
		RunElevator retractFrontLeg = new RunElevator(RunElevator.MoveType.FRONT, maxVelRetract, maxAccelRetract,
				extendDistance, 0, subsystem);
		DriveLegWheels nudgeLegsForwardFrontLegRetracted = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				nudge1Distance, nudge2Distance, subsystem);
//		RunDriveMP nudgeDriveForwardFrontLegRetracted = new RunDriveMP(maxVelNudge, maxAccelNudge,
//				0, nudge1Distance, drive);
		RunMotorUntilConditionMet crawlLegsForwardFrontLegRetracted = new RunMotorUntilConditionMet(subsystem, failsafe2);
//		DriveStraightUntilConditionMet crawlDriveForwardFrontLegRetracted = new DriveStraightUntilConditionMet(
//				2, null, 0, null, null,
//				0, false, 0, 0, 0, drive, failsafe2, 0.1);
		RunElevator retractBackLegPartially = new RunElevator(RunElevator.MoveType.BACK, maxVelRetract, maxAccelRetract,
				extendDistance, extendDistance - partialRetractionDistance, subsystem);

		addSequential(dropLegs);
		addParallel(nudgeLegsForwardLegsDropped);
//		addSequential(nudgeDriveForwardLegsDropped);
		addParallel(crawlLegsForwardLegsDropped);
//		addSequential(crawlDriveForwardLegsDropped);
		addSequential(retractFrontLeg);
		addParallel(nudgeLegsForwardFrontLegRetracted);
//		addSequential(nudgeDriveForwardFrontLegRetracted);
		addParallel(crawlLegsForwardFrontLegRetracted);
//		addSequential(crawlDriveForwardFrontLegRetracted);
		addSequential(retractBackLegPartially);
	}
}
