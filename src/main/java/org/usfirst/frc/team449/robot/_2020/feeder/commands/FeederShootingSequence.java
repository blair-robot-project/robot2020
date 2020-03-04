package org.usfirst.frc.team449.robot._2020.feeder.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;
import org.usfirst.frc.team449.robot._2020.shooter.DualFlywheel;

public class FeederShootingSequence extends SequentialCommandGroup {

    DualFlywheel flywheel;
    FeederCounting feeder;

    public FeederShootingSequence(FeederCounting feeder, DualFlywheel flywheel) {
        this.feeder = feeder;
        this.flywheel = flywheel;
        addRequirements(feeder);
        addCommands(new SetFeederState(feeder, FeederCounting.FeederState.PRESHOOTING),
                new WaitCommand(feeder.getPreShootingReverseDelay()),
                new SetFeederState(feeder, FeederCounting.FeederState.SHOOTING));
    }

    @Override
    public boolean isFinished() {
        return flywheel.getCurrentState() == DualFlywheel.DualFlywheelState.NEUTRAL;
    }

}
