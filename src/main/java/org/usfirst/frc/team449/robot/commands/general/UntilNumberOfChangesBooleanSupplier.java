package org.usfirst.frc.team449.robot.commands.general;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

public class UntilNumberOfChangesBooleanSupplier<T> implements BooleanSupplier {
    @NotNull
    private final UnaryOperator<T> getNewValue;
    private final int targetChanges;

    @Nullable
    private T currentValue;
    private int numChangesUndergone;

    /**
     * @param getNewValue   Accepts old value and returns {@code null} if no change and a new value if there was a change
     * @param targetChanges
     */
    // Hmm. We can override in Java by overriding, but we can also override with YAML by injecting functional interfaces.
    public UntilNumberOfChangesBooleanSupplier(final UnaryOperator<T> getNewValue,
                                               final int targetChanges) {
        this.getNewValue = getNewValue;
        this.targetChanges = targetChanges;
    }

    @Override
    public boolean getAsBoolean() {
        final T newValue = this.getNewValue.apply(this.currentValue);
        if (newValue != null) {
            this.currentValue = newValue;
            this.numChangesUndergone++;
        }

        return this.numChangesUndergone >= this.targetChanges;
    }
}
