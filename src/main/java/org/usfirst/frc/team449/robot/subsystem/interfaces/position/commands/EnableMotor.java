package org.usfirst.frc.team449.robot.subsystem.interfaces.position.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.position.SubsystemPosition;

/**
 * Enable the motors in the given subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class EnableMotor extends Command {

    /**
     * The subsystem to execute this command on.
     */
    @NotNull
    private final SubsystemPosition subsystem;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     */
    @JsonCreator
    public EnableMotor(@NotNull @JsonProperty(required = true) SubsystemPosition subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("EnableMotor init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("EnableMotor init.", this.getClass());
    }

    /**
     * Enables the motor.
     */
    @Override
    protected void execute() {
        subsystem.enableMotor();
    }

    /**
     * Finish immediately because this is a state-change command.
     *
     * @return true
     */
    @Override
    protected boolean isFinished() {
        return true;
    }

    /**
     * Log when this command ends
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("EnableMotor end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("EnableMotor end.", this.getClass());
    }

    /**
     * Log when this command is interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("EnableMotor interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("EnableMotor interrupted!", this.getClass());
    }
}