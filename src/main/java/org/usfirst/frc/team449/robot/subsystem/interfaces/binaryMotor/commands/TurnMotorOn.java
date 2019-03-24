package org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.SubsystemBinaryMotor;

/**
 * Turns on the motor of the specified subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class TurnMotorOn extends InstantCommand {

    /**
     * The subsystem to execute this command on.
     */
    @NotNull
    @Log.Exclude
    private final SubsystemBinaryMotor subsystem;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     */
    @JsonCreator
    public TurnMotorOn(@NotNull @JsonProperty(required = true) SubsystemBinaryMotor subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("TurnMotorOn init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("TurnMotorOn init.", this.getClass());
    }

    /**
     * Turn the motor on.
     */
    @Override
    protected void execute() {
        subsystem.turnMotorOn();
    }

    /**
     * Log when this command ends
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("TurnMotorOn end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("TurnMotorOn end.", this.getClass());
    }

    /**
     * Log when this command is interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("TurnMotorOn Interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("TurnMotorOn Interrupted!", this.getClass());
    }
}