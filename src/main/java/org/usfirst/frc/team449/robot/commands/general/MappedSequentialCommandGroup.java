package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MappedSequentialCommandGroup extends SequentialCommandGroup {
    @JsonCreator
    public MappedSequentialCommandGroup(@NotNull @JsonProperty(required = true) List<Command> commands,
                                        @Nullable List<Subsystem> requires) {
        super((Command[]) commands.toArray());
        if (requires != null)
            for (var subsystem : requires)
                this.addRequirements(subsystem);
    }
}
