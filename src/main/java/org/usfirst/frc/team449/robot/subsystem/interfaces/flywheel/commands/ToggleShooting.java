package org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.SubsystemFlywheel;

/**
 * Toggle whether or not the subsystem is firing.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ToggleShooting extends SequentialCommandGroup {


    /**
     * Default constructor.
     *
     * @param subsystem The subsystem to execute this command on.
     */
    @JsonCreator
    public ToggleShooting(@NotNull @JsonProperty(required = true) SubsystemFlywheel subsystem) {
        switch (subsystem.getFlywheelState()) {
            case OFF:
                addCommands(new SpinUpThenShoot(subsystem));
                break;
            case SHOOTING:
                addCommands(new TurnAllOff(subsystem));
                break;
            case SPINNING_UP:
                addCommands(new TurnAllOn(subsystem));
        }
    }
}
