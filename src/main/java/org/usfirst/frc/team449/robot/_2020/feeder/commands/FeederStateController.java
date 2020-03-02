package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.FlywheelSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.IntakeSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemFlywheel;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FeederStateController extends CommandBase {

    FlywheelSimple flywheel;
    IntakeSimple intake;
    FeederCounting feeder;

    @JsonCreator
    public FeederStateController(@JsonProperty(required = true) FeederCounting feeder){
//        @JsonProperty(required = true) IntakeSimple intake,
//        @JsonProperty(required = true) FeederCounting feeder
        this.feeder = feeder;
//        this.intake = intake;
//        this.flywheel = flywheel;
        addRequirements(feeder);
    }

    @Override
    public void execute(){
        if(flywheel.getFlywheelState() == SubsystemFlywheel.FlywheelState.SHOOTING){
            new FeederShootingSequence(feeder, flywheel).schedule();
        }

        if(intake.getMode() == SubsystemIntake.IntakeMode.IN_FAST) {
            new FeederIntakeOverride(feeder).schedule();
        }

        if(feeder.sensorsTripped()){
            feeder.setState(FeederCounting.FeederState.INTAKECOMPLETE);
        } else {
            feeder.setState(FeederCounting.FeederState.NEUTRAL);
        }
    }

    @Override
    public boolean isFinished(){
        return false;
    }

    @Override
    public void end(boolean interrupted){
        if(!interrupted){
            feeder.setState(FeederCounting.FeederState.NEUTRAL);
        }
    }

}
