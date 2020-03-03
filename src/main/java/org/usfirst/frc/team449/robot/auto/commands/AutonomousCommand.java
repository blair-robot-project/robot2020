package org.usfirst.frc.team449.robot.auto.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public interface AutonomousCommand extends Command {

    default void setRunTimeSeconds(){}

    default double getRunTimeSeconds(){ return 0; }

    default Command getTimedCommand(){
        return this.deadlineWith(new WaitCommand(getRunTimeSeconds()));
    }


}
