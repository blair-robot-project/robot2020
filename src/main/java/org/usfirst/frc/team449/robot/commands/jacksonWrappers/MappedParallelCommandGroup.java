package org.usfirst.frc.team449.robot.commands.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Runs a set of commands in parallel, ending when the last command ends.
 *
 * @see ParallelCommandGroup
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedParallelCommandGroup extends ParallelCommandGroup {
    /**
     * Creates a new ParallelCommandGroup.  The given commands will be executed simultaneously.
     * The command group will finish when the last command finishes.  If the CommandGroup is
     * interrupted, only the commands that are still running will be interrupted.
     *
     * @param commands           the commands to include in this group.
     * @param requiredSubsystems the list of subsystems that this command requires
     */
    @JsonCreator
    public MappedParallelCommandGroup(@NotNull @JsonProperty(required = true) final Command[] commands,
                                      @Nullable final Subsystem[] requiredSubsystems) {
        super(commands);
        if (requiredSubsystems != null) super.addRequirements(requiredSubsystems);
    }
}
