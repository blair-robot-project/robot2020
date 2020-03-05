package org.usfirst.frc.team449.robot._2020.climber.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.usfirst.frc.team449.robot._2020.climber.SafeWinchingClimber;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.commands.TurnMotorOn;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class StartClimber extends SequentialCommandGroup {

  SafeWinchingClimber climber;

  @JsonCreator
  public StartClimber(@JsonProperty(required = true) final SafeWinchingClimber climber) {
    this.climber = climber;
    addCommands(new SetClimberWithArmState(climber, SetClimberWithArmState.ClimberState.LOWER),
        new WaitCommand(2),
        new TurnMotorOn(climber));
  }

}
