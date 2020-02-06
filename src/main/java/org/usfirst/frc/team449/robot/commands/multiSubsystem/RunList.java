package org.usfirst.frc.team449.robot.commands.multiSubsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunList extends ParallelCommandGroup {

    @JsonCreator
    public RunList(@JsonProperty(required = true) List<Command> list) {
        addCommands(list.toArray(new Command[0]));
    }
}
