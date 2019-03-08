package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;

/**
 * A command that runs any number of {@link Runnable} objects every tic.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunRunnables extends Command {

    /**
     * The runnables to run.
     */
    @NotNull
    private final Runnable[] runnables;

    /**
     * Default constructor
     *
     * @param runnables The runnables to run.
     */
    @JsonCreator
    public RunRunnables(@NotNull @JsonProperty(required = true) MappedRunnable[] runnables) {
        this.runnables = runnables;
    }

    /**
     * Log on init
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("RunRunnables init", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    /**
     * Run all the runnables in the order they were given.
     */
    @Override
    protected void execute() {
        for (Runnable runnable : runnables) {
            runnable.run();
        }
    }

    /**
     * @return false
     */
    @Override
    protected boolean isFinished() {
        //This does NOT have to be true.
        return false;
    }

    /**
     * Log on exit.
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("RunRunnables end", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    /**
     * Log when interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("RunRunnables interrupted", this.getClass().getSimpleName(), EventImportance.kNormal);
    }
}