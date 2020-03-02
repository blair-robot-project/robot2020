package org.usfirst.frc.team449.robot._2020.feeder.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.FlywheelSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemFlywheel;

public class FeederShootingSequence extends SequentialCommandGroup {

    FlywheelSimple flywheel;
    FeederCounting feeder;

    public FeederShootingSequence(FeederCounting feeder, FlywheelSimple flywheel){
        this.feeder = feeder;
        this.flywheel = flywheel;
        addRequirements(feeder);
        addCommands(new SetFeederState(feeder, FeederCounting.FeederState.HOLDINGFIVE));
    }

    @Override
    public boolean isFinished(){
        return flywheel.getFlywheelState() == SubsystemFlywheel.FlywheelState.OFF;
    }

}
