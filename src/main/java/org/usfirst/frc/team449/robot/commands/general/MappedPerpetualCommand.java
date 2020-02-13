package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MappedPerpetualCommand extends PerpetualCommand {
    /**
     * Creates a new PerpetualCommand.  Will run another command in perpetuity, ignoring that
     * command's end conditions, unless this command itself is interrupted.
     *
     * @param command the command to run perpetually
     */
    @JsonCreator
    public MappedPerpetualCommand(@NotNull @JsonProperty(required = true) Command command,
                                  @Nullable List<Subsystem> requires) {
        super(command);
        if (requires != null)
            for (var subsystem : requires)
                this.addRequirements(subsystem);
    }
}