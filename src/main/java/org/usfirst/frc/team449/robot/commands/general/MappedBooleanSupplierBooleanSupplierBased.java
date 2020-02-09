package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DigitalInput;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.SubsystemConditional;

import java.util.function.BooleanSupplier;

/**
 * Intended to be used as a base class.
 */
public class MappedBooleanSupplierBooleanSupplierBased implements BooleanSupplier {
    private final BooleanSupplier source;

    /**
     * Default constructor.
     */
    @JsonCreator
    public MappedBooleanSupplierBooleanSupplierBased(@NotNull @JsonProperty(required = true) BooleanSupplier supplier) {
        this.source = supplier;
    }

    @Override
    public final boolean getAsBoolean() {
        return this.source.getAsBoolean();
    }
}
