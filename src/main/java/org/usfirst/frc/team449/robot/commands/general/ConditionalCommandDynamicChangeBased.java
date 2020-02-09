package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

/**
 * Runs one of two commands the first tick on which the given condition becomes true.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ConditionalCommandDynamicChangeBased extends ConditionalCommandDynamic {
    /**
     * Default constructor
     *
     * @param afterBecomingTrue  the Command to execute if BooleanSupplier returns begins returning true
     * @param afterBecomingFalse the Command to execute if BooleanSupplier returns begins returning false
     * @param booleanSupplier    a method for determining which command to run
     */
    @JsonCreator
    public ConditionalCommandDynamicChangeBased(@Nullable final Command afterBecomingTrue,
                                                @Nullable final Command afterBecomingFalse,
                                                @NotNull @JsonProperty(required = true) final BooleanSupplierUpdatable booleanSupplier,
                                                @Nullable final Subsystem[] requiredSubsystems) {
        super(
                new RunRunnables(
                        false,
                        () -> {
                            if (booleanSupplier.getAsBoolean()) {
                                if (afterBecomingTrue != null) afterBecomingTrue.execute();
                            } else {
                                if (afterBecomingFalse != null) afterBecomingFalse.execute();
                            }
                        }),

                // Don't do anything when the condition isn't changing.
                null,

                // Only run when
                new BooleanSupplier() {
                    private long lastTime;
                    private boolean lastState;

                    @Override
                    public boolean getAsBoolean() {
                        booleanSupplier.update();

                        final boolean current = booleanSupplier.getAsBoolean();
                        final boolean stateChanged = current != this.lastState;
                        this.lastState = current;
                        return stateChanged;
                    }
                },
                requiredSubsystems);
    }
}
