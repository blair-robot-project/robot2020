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
import org.usfirst.frc.team449.robot.drive.unidirectional.commands.DriveAtSpeed;
import org.usfirst.frc.team449.robot.drive.unidirectional.commands.RunDriveMP;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.commands.TurnMotorOff;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.commands.TurnMotorOffWithRequires;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.commands.TurnMotorOn;
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
	 * @param extendDistance   How far the elevators should extend, in feet.
	 * @param nudge1Distance   How far to nudge the robot forward after the legs both extend, in feet.
	 * @param nudge2Distance   How far to nudge the robot forward after the front leg retracts, in feet.
	 * @param nudge3Distance   How far to nudge the robot forward after both legs are retracted, in feet.
	 * @param offset           How much the front elevator should extend further than the back elevator, in feet.
	 * @param velReduction     How much to reduce the max velocity of the back elevator profile, as a percent.
	 * @param accelReduction   How much to reduce the max acceleration of the back elevator profile, as a percent.
	 * @param unstickTolerance How far to make the elevator extend before retracting, to unstick the brake, in feet.
	 * @param voltageBack  The voltage to give to stall the back elevator.
	 * @param voltageFront The voltage to give to stall the front elevator.
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
	             @JsonProperty(required = true) double extendDistance,
	             @JsonProperty(required = true) double nudge1Distance,
	             @JsonProperty(required = true) double nudge2Distance,
	             @JsonProperty(required = true) double nudge3Distance,
	             double offset,
	             double velReduction,
	             double accelReduction,
	             @Nullable Double unstickTolerance,
				 double voltageBack,
	             double voltageFront,
				 double crawlVelocity) {
		requires(climber);
		climber.setCrawlVelocity(crawlVelocity);


		RunElevator extendLegs = new RunElevator(RunElevator.MoveType.BOTH, maxVelExtend, maxAccelExtend,
				0, extendDistance, offset, velReduction, accelReduction, null, climber);

		StallElevators stallElevators = new StallElevators(climber, voltageBack, voltageFront);

		DriveLegWheels nudgeLegsForwardLegsExtended = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				nudge1Distance, climber);
		RunDriveMP nudgeDriveForwardLegsExtended = new RunDriveMP<>(maxVelNudge, maxAccelNudge,
				-nudge1Distance, drive);

		RunElevator retractFrontLeg = new RunElevator(RunElevator.MoveType.FRONT, maxVelRetract, maxAccelRetract,
				extendDistance + offset, 0, 0, 0, 0, unstickTolerance, climber);

		DriveLegWheels nudgeLegsForwardFrontLegRetracted = new DriveLegWheels(maxVelNudge, maxAccelNudge,
				nudge2Distance, climber);
		RunDriveMP nudgeDriveForwardFrontLegRetracted = new RunDriveMP<>(maxVelNudge, maxAccelNudge,
				-nudge2Distance, drive);

		DriveAtSpeed crawlDrive = new DriveAtSpeed<>(drive, -crawlVelocity / 3., 5);
		TurnMotorOn crawlLeg = new TurnMotorOn(climber);

		RunElevator retractBackLeg = new RunElevator(RunElevator.MoveType.BACK, maxVelRetract, maxAccelRetract,
				extendDistance, 0, 0, 0, 0, null, climber);

		TurnMotorOffWithRequires stopLegCrawl = new TurnMotorOffWithRequires<>(climber);

		RunDriveMP nudgeDriveForwardLegsRetracted = new RunDriveMP<>(maxVelNudge, maxAccelNudge,
				-nudge3Distance, drive);


		addSequential(extendLegs);
		addSequential(stallElevators);
		addParallel(nudgeLegsForwardLegsExtended);
		addSequential(nudgeDriveForwardLegsExtended);
		addSequential(retractFrontLeg);
		addParallel(nudgeLegsForwardFrontLegRetracted);
		addSequential(nudgeDriveForwardFrontLegRetracted);
		addParallel(crawlDrive);
		addParallel(crawlLeg);
		addSequential(retractBackLeg);
		addSequential(stopLegCrawl);
		addSequential(nudgeDriveForwardLegsRetracted);
	}
}
