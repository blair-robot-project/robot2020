package org.usfirst.frc.team449.robot.drive.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.drive.DriveSubsystem;


/**
 * Stops the motors of the given drive subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class StopMotors extends InstantCommand {

    /**
     * The subsystem to execute this command on.
     */
    @NotNull
    private final DriveSubsystem subsystem;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     */
    @JsonCreator
    public StopMotors(@NotNull @JsonProperty(required = true) DriveSubsystem subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("StopMotors init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        Shuffleboard.addEventMarker("StopMotors init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("StopMotors init.", this.getClass());
    }

    /**
     * Do the state change.
     */
    @Override
    protected void execute() {
        subsystem.fullStop();
    }

    /**
     * Log when this command ends
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("StopMotors end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("StopMotors end.", this.getClass());
    }

    /**
     * Log when this command is interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("StopMotors Interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("StopMotors Interrupted!", this.getClass());
    }
}