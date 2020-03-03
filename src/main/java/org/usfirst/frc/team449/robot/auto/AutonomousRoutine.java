package org.usfirst.frc.team449.robot.auto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.usfirst.frc.team449.robot.auto.commands.AutonomousCommand;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class AutonomousRoutine extends SequentialCommandGroup {

    double executionTime = 0;

    @JsonCreator
    public AutonomousRoutine(List<AutonomousCommand> commandList){
        for(AutonomousCommand command : commandList){
            addCommands(command.getTimedCommand());
            executionTime += command.getRunTimeSeconds();
        }
        if(executionTime >= 15){
            DriverStation.reportWarning("The selected autonomous routine exceeds an execution time of 15 seconds" +
                    " Optimize the routine or it won't finish during play!",false);
        }
    }

}
