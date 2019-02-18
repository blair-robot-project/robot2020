package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.commands.multiInterface.RunMotorUntilConditionMet;
import org.usfirst.frc.team449.robot.commands.multiSubsystem.DriveStraightUntilConditionMet;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectionalWithGyro;
import org.usfirst.frc.team449.robot.drive.unidirectional.commands.RunDriveMP;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.IRWithButtonOverride;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

/**
 * Run a full 2019 climb sequence.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class Climb extends CommandGroup {

	/**
	 * Default constructor.
	 * 
	 * @param climber          The climber subsystem.
	 * @param drive            The robot drive.
	 * @param maxVelExtend     The maximum velocity for extending the legs.
	 * @param maxAccelExtend   The maximum acceleration for extending the legs.
	 * @param maxVelRetract    The maximum velocity for retracting the legs.
	 * @param maxAccelRetract  The maximum acceleration for retracting the legs.
	 * @param maxVelNudge      The maximum velocity for nudging forward via profile.
	 * @param maxAccelNudge    The maximum velocity for nudging forward via profile.
	 * @param crawlVelocity    The velocity for crawling forward until the IR trips.
	 * @param extendDistance   How far the elevators should extend, in feet.
	 * @param nudge1Distance   How far to nudge the robot forward after the legs both extend, in feet.
	 * @param nudge2Distance   How far to nudge the robot forward after the front leg retracts, in feet.
	 * @param nudge3Distance   How far to nudge the robot forward after both legs are retracted, in feet.
	 * @param offset           How much the front elevator should extend further than the back elevator, in feet.
	 * @param unstickTolerance How far to make the elevator extend before retracting, to unstick the brake, in feet.
	 * @param failsafe1        The IR sensor with button override which must return true before the front leg may retract.
	 * @param failsafe2        The IR sensor with button override which must return true before the back leg may retract.
	 */
	@JsonCreator
	public Climb(@JsonProperty(required = true) @NotNull SubsystemClimber2019 climber,
	             @JsonProperty(required = true) @NotNull DriveUnidirectionalWithGyro drive,
	             @JsonProperty(required = true) double maxVelExtend,
	             @JsonProperty(required = true) double maxAccelExtend,
	             @JsonProperty(required = true) double maxVelRetract,
	             @JsonProperty(required = true) double maxAccelRetract,
	             @JsonProperty(required = true) double maxVelNudge,
	             @JsonProperty(required = true) double maxAccelNudge,
	             @JsonProperty(required = true) double crawlVelocity,
	             @JsonProperty(required = true) double extendDistance,
	             @JsonProperty(required = true) double nudge1Distance,
	             @JsonProperty(required = true) double nudge2Distance,
	             @JsonProperty(required = true) double nudge3Distance,
	             double offset,
	             @Nullable Double unstickTolerance,
	             @JsonProperty(required = true) @NotNull IRWithButtonOverride failsafe1,
	             @JsonProperty(required = true) @NotNull IRWithButtonOverride failsafe2) {
		requires(climber);
		climber.setCrawlVelocity(crawlVelocity);


		RunElevator extendLegs = new RunElevator(RunElevator.MoveType.BOTH, maxVelExtend, maxAccelExtend,
				0, extendDistance, offset, null, climber);

		DriveLegWheels nudgeLegsForwardLegsExtended = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				nudge1Distance, climber);
		RunDriveMP nudgeDriveForwardLegsExtended = new RunDriveMP<>(maxVelNudge, maxAccelNudge,
				-nudge1Distance, drive);

		RunMotorUntilConditionMet crawlLegsForwardLegsExtended = new RunMotorUntilConditionMet<>(climber, failsafe1);
		DriveStraightUntilConditionMet crawlDriveForwardLegsExtended = new DriveStraightUntilConditionMet<>(
				2, null, 0, null, null,
				0, false, 0, 0, 0, drive, failsafe1, -crawlVelocity);

		RunElevator retractFrontLeg = new RunElevator(RunElevator.MoveType.FRONT, maxVelRetract, maxAccelRetract,
				extendDistance + offset, 0, 0, unstickTolerance, climber);

		DriveLegWheels nudgeLegsForwardFrontLegRetracted = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				nudge2Distance, climber);
		RunDriveMP nudgeDriveForwardFrontLegRetracted = new RunDriveMP<>(maxVelNudge, maxAccelNudge,
				-nudge2Distance, drive);

		RunMotorUntilConditionMet crawlLegsForwardFrontLegRetracted = new RunMotorUntilConditionMet<>(climber, failsafe2);
		DriveStraightUntilConditionMet crawlDriveForwardFrontLegRetracted = new DriveStraightUntilConditionMet<>(
				2, null, 0, null, null,
				0, false, 0, 0, 0, drive, failsafe2, -crawlVelocity);

		RunElevator retractBackLeg = new RunElevator(RunElevator.MoveType.BACK, maxVelRetract, maxAccelRetract,
				extendDistance, 0, 0, unstickTolerance, climber);

		RunDriveMP nudgeDriveForwardLegsRetracted = new RunDriveMP<>(maxVelNudge, maxAccelNudge,
				-nudge3Distance, drive);


		addSequential(extendLegs);
		addParallel(nudgeLegsForwardLegsExtended);
		addSequential(nudgeDriveForwardLegsExtended);
		addParallel(crawlLegsForwardLegsExtended);
		addSequential(crawlDriveForwardLegsExtended);
		addSequential(retractFrontLeg);
		addParallel(nudgeLegsForwardFrontLegRetracted);
		addSequential(nudgeDriveForwardFrontLegRetracted);
		addParallel(crawlLegsForwardFrontLegRetracted);
		addSequential(crawlDriveForwardFrontLegRetracted);
		addSequential(retractBackLeg);
		addSequential(nudgeDriveForwardLegsRetracted);
	}
}
