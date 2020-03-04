package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.usfirst.frc.team449.robot._2020.feeder.FeederCounting;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class SetFeederState extends InstantCommand {

    FeederCounting feeder;
    FeederCounting.FeederState state;

    @JsonCreator
    public SetFeederState(@JsonProperty(required = true) FeederCounting feeder,
                          @JsonProperty(required = true) FeederCounting.FeederState state) {
        this.feeder = feeder;
        this.state = state;
    }

    @Override
    public void execute() {
        feeder.setState(state);
    }

}
