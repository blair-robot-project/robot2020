package org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.DummyAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;

public class SetHeadingCurrentReading extends InstantCommand {

    /**
     * The subsystem to execute this command on.
     */
    @NotNull
    @Log.Exclude
    private final DummyAHRS subsystem;

    DummyPIDAngleCommand command;


    /**
     * Default constructor.
     *
     * @param subsystem  The subsystem to execute this command on.
     */
    @JsonCreator
    public SetHeadingCurrentReading(@NotNull @JsonProperty(required = true) DummyAHRS subsystem, DummyPIDAngleCommand command) {
        this.subsystem = subsystem;
        this.command = command;
    }

    /**
     * Log on init.
     */
    @Override
    public void initialize() {
        Shuffleboard.addEventMarker("SetHeading init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("SetHeading init.", this.getClass());
    }

    /**
     * Set the heading.
     */
    @Override
    public void execute() {
        command.setSetpoint(subsystem.getHeadingCached());
    }

    /**
     * Log on exit.
     */
    @Override
    public void end(boolean interrupted) {
        Shuffleboard.addEventMarker("SetHeadingCurrentReading end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("SetHeading end.", this.getClass());
    }
}
