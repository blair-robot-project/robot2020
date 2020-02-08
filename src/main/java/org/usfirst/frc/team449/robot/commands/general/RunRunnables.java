package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;

import java.util.Objects;
import java.util.Set;

/**
 * A command that runs any number of {@link Runnable} objects every tick.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunRunnables extends CommandBase {

    /**
     * The runnables to run.
     */
    @NotNull
    private final Runnable[] runnables;
    private final boolean keepRunning;

    /**
     * Default constructor
     *
     * @param runnables The runnables to run.
     */
    @JsonCreator
    public RunRunnables(@NotNull @JsonProperty(required = true) Runnable[] runnables,
                        @Nullable Boolean keepRunning) {
        this.runnables = runnables;
        this.keepRunning = keepRunning != null ? keepRunning : true;
    }

    /**
     * Log on init
     */
    @Override
    public void initialize() {
        Shuffleboard.addEventMarker("RunRunnables init", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    /**
     * Run all the runnables in the order they were given.
     */
    @Override
    public void execute() {
        for (Runnable runnable : runnables) {
            runnable.run();
        }
    }

    /**
     * @return false
     */
    @Override
    public boolean isFinished() {
        //This does NOT have to be true.
        return !this.keepRunning;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        // This shouldn't return null.
        return Set.of();
    }

    /**
     * Log on exit.
     */
    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            Shuffleboard.addEventMarker("RunRunnables interrupted", this.getClass().getSimpleName(), EventImportance.kNormal);
        }
        Shuffleboard.addEventMarker("RunRunnables end", this.getClass().getSimpleName(), EventImportance.kNormal);
    }
}