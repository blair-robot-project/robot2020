package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

/**
 * A command that runs until the specified {@link BooleanSupplier} returns {@code true}.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class WaitUntilConditionCommand extends CommandBase {
    private final BooleanSupplier booleanSupplier;

    /**
     * Default constructor.
     *
     * @param booleanSupplier A method for determining when the command should finish.
     */
    @JsonCreator
    public WaitUntilConditionCommand(@NotNull @JsonProperty(required = true) final BooleanSupplier booleanSupplier) {
        this.booleanSupplier = booleanSupplier;
    }

    /**
     * Whether the command has finished.  Once a command finishes, the scheduler will call its
     * end() method and un-schedule it.
     *
     * @return whether the command has finished.
     */
    @Override
    public boolean isFinished() {
        return this.booleanSupplier.getAsBoolean();
    }
}
