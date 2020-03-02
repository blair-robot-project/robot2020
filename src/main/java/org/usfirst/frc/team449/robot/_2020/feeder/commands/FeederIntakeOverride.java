package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.IntakeSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FeederIntakeOverride extends CommandBase {

    FeederCounting feeder;
    IntakeSimple intake;

    @JsonCreator
    public FeederIntakeOverride(@JsonProperty(required = true) FeederCounting feeder, IntakeSimple intake) {
        this.feeder = feeder;
        addRequirements(feeder);
    }

    @Override
    public void execute() {
        if (feeder.sensorsTripped()) {
            feeder.setState(FeederCounting.FeederState.INTAKECOMPLETE);
        } else {
            feeder.setState(FeederCounting.FeederState.INTAKEOVERRIDE);
        }
    }

    @Override
    public boolean isFinished() {
        return intake.getMode() == SubsystemIntake.IntakeMode.OFF;
    }

}
