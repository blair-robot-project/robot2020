package org.usfirst.frc.team449.robot.subsystem.interfaces.position.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;

import org.usfirst.frc.team449.robot.subsystem.interfaces.position.SubsystemPosition;

import java.security.spec.ECField;

/**
 * Go to a given position
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class GoToPosition<T extends Subsystem & SubsystemPosition> extends Command {

    /**
     * The subsystem to execute this command on.
     */
    @NotNull
    @Log.Exclude
    private final T subsystem;

    /**
     * The position to go to, in feet.
     */
    private double setpoint;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     * @param setpoint  The position to go to, in feet.
     */
    @JsonCreator
    public GoToPosition(@NotNull @JsonProperty(required = true) T subsystem,
                        @JsonProperty(required = true) double setpoint) {
        requires(subsystem);
        this.subsystem = subsystem;
        this.setpoint = setpoint;
    }

    /**
     * Log and set setpoint when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("GoToPosition init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("GoToPosition init.", this.getClass());
        subsystem.setPositionSetpoint(setpoint);
    }

    /**
     * Does nothing, don't want to spam position setpoints.
     */
    @Override
    protected void execute() {
        // Do nothing
    }

    /**
     * Exit when the setpoint has been reached
     *
     * @return true if the setpoint is reached, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        return subsystem.onTarget();
    }

    /**
     * Log when this command ends
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("GoToPosition end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("GoToPosition end.", this.getClass());
    }

    /**
     * Log when this command is interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("GoToPosition interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("GoToPosition interrupted!", this.getClass());
    }
}
