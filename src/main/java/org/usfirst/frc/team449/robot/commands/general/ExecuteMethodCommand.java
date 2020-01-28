package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.Util;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ExecuteMethodCommand extends InstantCommand {

    @JsonCreator
    public ExecuteMethodCommand(@NotNull @JsonProperty(required=true) Object object,
                                @NotNull @JsonProperty(required=true) String methodName,
                                @NotNull @JsonProperty(required=true) List<Subsystem> requirements) {
        super(Util.getMethod(object, methodName), requirements.toArray(new Subsystem[0]));
    }
}
