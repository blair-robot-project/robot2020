package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SetFeederState extends InstantCommand {

    FeederCounting feeder;
    FeederCounting.FeederState feederState;

    @JsonCreator
    public SetFeederState(@JsonProperty(required = true) FeederCounting feeder,
                          @JsonProperty(required = true) FeederCounting.FeederState feederState){
        this.feeder = feeder;
        this.feederState = feederState;
    }

    @Override
    public void execute(){
        feeder.setState(feederState);
    }

}
