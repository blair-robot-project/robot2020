package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.jetbrains.annotations.Nullable;

/**
 * A command that does nothing; used for debugging or if a command is required somewhere but none is desired.
 */
public class PlaceholderCommand extends CommandBase {
    private final String debugMessage;

    /**
     * Constructs a placeholder command that logs the specified message (if not null) when executed.
     *
     * @param debugMessage the message to print every time this command is executed; null to not do anything
     */
    @JsonCreator
    public PlaceholderCommand(@Nullable final String debugMessage) {
        this.debugMessage = debugMessage;
    }

    /**
     * Constructs a placeholder command that does nothing when executed.
     */
    public PlaceholderCommand() {
        this(null);
    }

    @Override
    public void execute() {
        if (this.debugMessage != null)
            System.out.println(this.debugMessage);
    }
}
