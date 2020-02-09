package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.SubsystemConditional;

import java.util.function.BooleanSupplier;

/**
 * A condition based on the condition of a conditional subsystem.
 */
public class MappedBooleanSupplierSubsystemBased extends MappedBooleanSupplierBooleanSupplierBased {
    /**
     * Default constructor.
     *
     * @param subsystem The conditional subsystem on whose condition the condition depends.
     * @param useCached If true, use {@link SubsystemConditional#isConditionTrueCached()};
     *                  otherwise, use {@link SubsystemConditional#isConditionTrue()}
     */
    @JsonCreator
    public MappedBooleanSupplierSubsystemBased(@NotNull @JsonProperty(required = true) SubsystemConditional subsystem,
                                               boolean useCached) {
        super(useCached ? subsystem::isConditionTrueCached : subsystem::isConditionTrue);
    }
}
