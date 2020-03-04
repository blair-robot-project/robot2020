package org.usfirst.frc.team449.robot._2020.intake.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.usfirst.frc.team449.robot._2020.intake.IntakeDeployed;
import org.usfirst.frc.team449.robot.auto.commands.AutonomousCommand;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class SetIntakeState extends InstantCommand implements AutonomousCommand {

    IntakeDeployed intake;
    IntakeDeployed.IntakeState state;

    @JsonCreator
    public SetIntakeState(@JsonProperty(required = true) IntakeDeployed intake,
                          @JsonProperty(required = true) IntakeDeployed.IntakeState state){
        this.intake = intake;
        this.state = state;
        addRequirements(intake);
    }

    @Override
    public void execute(){
        intake.setIntakeState(state);
    }

}
