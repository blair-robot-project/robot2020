package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A command that either does nothing or prints a message when run. Used for debugging or if a command is required somewhere but no side effects are desired.
 */
public class PlaceholderCommand extends InstantCommand {
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

    /**
     * The initial subroutine of a command.  Called once when the command is initially scheduled.
     */
    @Override
    public void initialize() {
        if (this.debugMessage != null)
            System.out.println(this.debugMessage);
    }

    /**
     * Specifies the set of subsystems used by this command.  Two commands cannot use the same
     * subsystem at the same time.  If the command is scheduled as interruptible and another
     * command is scheduled that shares a requirement, the command will be interrupted.  Else,
     * the command will not be scheduled.  If no subsystems are required, return an empty set.
     *
     * <p>Note: it is recommended that user implementations contain the requirements as a field,
     * and return that field here, rather than allocating a new set every time this is called.
     *
     * @return the set of subsystems that are required
     */
    @Override
    public Set<Subsystem> getRequirements() {
        return Set.of();
    }

    /**
     * The singleton instance.
     */
    private static final PlaceholderCommand instance = new PlaceholderCommand();

    /**
     * Returns a default instance that does nothing when executed.
     *
     * @return a default instance
     */
    public static PlaceholderCommand getInstance() {
        return instance;
    }
}
