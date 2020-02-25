package org.usfirst.frc.team449.robot._2020.feeder.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;

public class RunSoWeCanOverrideTheDefaultCommand<T extends Subsystem & SubsystemIntake> extends CommandBase {

    T subsystem;
    SubsystemIntake.IntakeMode mode;

    public RunSoWeCanOverrideTheDefaultCommand(T subsystem, SubsystemIntake.IntakeMode mode){
        addRequirements(subsystem);
        this.subsystem = subsystem;
        this.mode = mode;
    }

    public void execute(){
        subsystem.setMode(mode);
    }

    public boolean isFinished(){
        return false;
    }
}
