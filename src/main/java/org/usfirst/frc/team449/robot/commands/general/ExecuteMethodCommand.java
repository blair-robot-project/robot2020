package org.usfirst.frc.team449.robot.commands.general;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.usfirst.frc.team449.robot.other.Util;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ExecuteMethodCommand extends InstantCommand {

    public ExecuteMethodCommand(Object object, String methodName, List<Subsystem> requirements) {
        super(Util.getMethod(object, methodName), (Subsystem[]) requirements.toArray());
    }
}
