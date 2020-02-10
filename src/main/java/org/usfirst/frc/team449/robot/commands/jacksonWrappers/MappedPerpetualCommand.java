package org.usfirst.frc.team449.robot.commands.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Runs another command in perpetuity, ignoring that command's end conditions.
 *
 * @see PerpetualCommand
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedPerpetualCommand extends PerpetualCommand {
    /**
     * Creates a new PerpetualCommand.  Will run another command in perpetuity, ignoring that
     * command's end conditions, unless this command itself is interrupted.
     *
     * @param command the command to run perpetually
     * @param requiredSubsystems the list of subsystems that this command requires
     */
    @JsonCreator
    public MappedPerpetualCommand(@NotNull @JsonProperty(required = true) final Command command,
                                  @Nullable final List<Subsystem> requiredSubsystems) {
        // TODO: We should requireNonNull such parameters because map errors can cause them to be null.
        super(command);
        if (requiredSubsystems != null)
            for (final var subsystem : requiredSubsystems)
                this.addRequirements(subsystem);
    }
}
