package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

/**
 * A generic ConditionalCommand that takes a lambda for determining which command to run.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ConditionalCommandFunctional extends ConditionalCommand {
    /**
     * Default constructor
     *
     * @param onTrue          The Command to execute if BooleanSupplier returns true
     * @param onFalse         The Command to execute if BooleanSupplier returns false. Can be null to not execute a
     *                        command if the supplier is false.
     * @param booleanSupplier A method for determining which command to run.
     */
    @JsonCreator
    public ConditionalCommandFunctional(@NotNull @JsonProperty(required = true) Command onTrue,
                                        @Nullable Command onFalse,
                                        @NotNull @JsonProperty(required = true) MappedBooleanSupplier booleanSupplier) {
        super(onTrue, onFalse, booleanSupplier);
    }
}
