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

import java.security.spec.ECField;

/**
 * Disable the motors in the given subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DisableMotor extends Command {

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
    public DisableMotor(@NotNull @JsonProperty(required = true) SubsystemPosition subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("DisableMotor init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("DisableMotor init.", this.getClass());
    }

    /**
     * Disables the motor.
     */
    @Override
    protected void execute() {
        subsystem.disableMotor();
    }

    /**
     * Finish immediately because this is a state-change command.
     *
     * @return false
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
        Shuffleboard.addEventMarker("DisableMotor end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("DisableMotor end.", this.getClass());
    }

    /**
     * Log when this command is interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("DisableMotor interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("DisableMotor interrupted!", this.getClass());
    }
}