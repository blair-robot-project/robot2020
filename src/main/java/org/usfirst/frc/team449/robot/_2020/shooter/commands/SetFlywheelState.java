package org.usfirst.frc.team449.robot._2020.shooter.commands;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.usfirst.frc.team449.robot._2020.shooter.DualFlywheel;
import org.usfirst.frc.team449.robot.auto.commands.AutonomousCommand;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class SetFlywheelState extends InstantCommand implements AutonomousCommand {
    DualFlywheel flywheel;
    DualFlywheel.DualFlywheelState state;

    @JsonCreator
    public SetFlywheelState(@JsonProperty(required = true) DualFlywheel flywheel,
                            @JsonProperty(required = true) DualFlywheel.DualFlywheelState state) {
        this.flywheel = flywheel;
        this.state = state;
        addRequirements(flywheel);
    }

    @Override
    public void execute() {
        flywheel.setFlywheelState(state);
    }
}
