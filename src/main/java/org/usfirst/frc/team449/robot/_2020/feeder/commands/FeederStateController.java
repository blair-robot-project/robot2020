package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;
import org.usfirst.frc.team449.robot._2020.intake.IntakeDeployed;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot._2020.shooter.DualFlywheel;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FeederStateController extends CommandBase {

    DualFlywheel flywheel;
    IntakeDeployed intake;
    FeederCounting feeder;

    @JsonCreator
    public FeederStateController(@JsonProperty(required = true) FeederCounting feeder,
                                 @JsonProperty(required = true) DualFlywheel flywheel,
                                 @JsonProperty(required = true) IntakeDeployed intake) {
        this.feeder = feeder;
        this.intake = intake;
        this.flywheel = flywheel;
        addRequirements(feeder);
    }

    @Override
    public void execute() {
        if (flywheel.getCurrentState() == DualFlywheel.DualFlywheelState.SHOOTING) {
            new FeederShootingSequence(feeder, flywheel).schedule();
        }

        if (intake.getCurrentState() == IntakeDeployed.IntakeState.FORWARD) {
            new FeederIntakeOverride(feeder, intake).schedule();
        }

        if (feeder.getBallPresent()) {
            feeder.setState(FeederCounting.FeederState.INTAKECOMPLETE);
        } else {
            feeder.setState(FeederCounting.FeederState.NEUTRAL);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        if (!interrupted) {
            feeder.setState(FeederCounting.FeederState.NEUTRAL);
        }
    }

}
