package org.usfirst.frc.team449.robot._2020.multiSubsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

/**
 * Intended to be used as a base class.
 */
public class BooleanSupplierBooleanSupplierBased implements BooleanSupplier {
    private final BooleanSupplier source;

    /**
     * Default constructor.
     */
    @JsonCreator
    public BooleanSupplierBooleanSupplierBased(
            @NotNull @JsonProperty(required = true) final BooleanSupplier supplier) {
        this.source = supplier;
    }

    @Override
    public final boolean getAsBoolean() {
        return this.source.getAsBoolean();
    }
}
