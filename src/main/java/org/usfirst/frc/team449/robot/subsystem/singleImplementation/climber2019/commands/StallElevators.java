package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.jetbrains.annotations.NotNull;

import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

public class StallElevators extends InstantCommand {

	/**
	 * The climber subsystem.
	 */
	@NotNull
	private SubsystemClimber2019 climber;

	/**
	 * The voltages to give to each elevator
	 */
	private double voltageBack, voltageFront;

	/**
	 * Default constructor
	 *
	 * @param climber      The climber subsystem.
	 * @param voltageBack  The voltage to give to the back elevator.
	 * @param voltageFront The voltage to give to the front elevator.
	 */
	public StallElevators(@NotNull SubsystemClimber2019 climber, double voltageBack, double voltageFront) {
		this.climber = climber;
		this.voltageBack = voltageBack;
		this.voltageFront = voltageFront;
	}

	/**
	 * Log on initialize.
	 */
	@Override
	protected void initialize() {
		Logger.addEvent("StallElevators init", this.getClass());
	}

	/**
	 * Stall both elevators
	 */
	@Override
	protected void execute() {
		climber.setBackVoltage(voltageBack);
		climber.setFrontVoltage(voltageFront);
	}

	/**
	 * Log on end.
	 */
	public void end() {
		Logger.addEvent("StallElevators end", this.getClass());
	}

}
