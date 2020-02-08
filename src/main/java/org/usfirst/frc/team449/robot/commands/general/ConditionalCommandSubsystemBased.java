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

/**
 * A conditional command that picks which command to run based on the condition of a conditional subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ConditionalCommandSubsystemBased extends ConditionalCommand {

    /**
     * Default constructor.
     *
     * @param onTrue  The Command to execute before the given time. Can be null to not run a command before.
     * @param onFalse The Command to execute after the given time. Can be null to not run a command after.
     * @param subsystem    The conditional subsystem on whose condition the command depends.
     * @param useCached    If true, use {@link SubsystemConditional#isConditionTrueCached()};
     *                     otherwise, use {@link SubsystemConditional#isConditionTrue()}
     */
    @JsonCreator
    public ConditionalCommandSubsystemBased(@Nullable Command onTrue,
                                            @Nullable Command onFalse,
                                            @NotNull @JsonProperty(required = true) SubsystemConditional subsystem,
                                            boolean useCached) {
        super(onTrue, onFalse, useCached ? subsystem::isConditionTrueCached : subsystem::isConditionTrue);
    }
}
