package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.Util;

import java.util.List;

/**
 * A Command that invokes a method on a given object by name using reflection.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class InvokeMethodByNameCommand extends InstantCommand {
    /**
     * Creates a new InvokeMethodByNameCommand that invokes the method with the specified name on the specified object
     * with the specified requirements.
     *
     * @param object       the object instance whose method to invoke
     * @param methodName   the name of the method to invoke
     * @param requirements the subsystems required by this command
     */
    @JsonCreator
    public InvokeMethodByNameCommand(@NotNull @JsonProperty(required = true) Object object,
                                     @NotNull @JsonProperty(required = true) String methodName,
                                     @Nullable List<Subsystem> requirements) {
        super(Util.getMethod(object, methodName), requirements != null ? requirements.toArray(new Subsystem[0]) : null);
    }
}
