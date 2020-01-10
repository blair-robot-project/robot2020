package org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.SubsystemFlywheel;

/**
 * Toggle whether or not the subsystem is firing.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ToggleShooting extends InstantCommand {
    /**
     * Default constructor.
     *
     * @param subsystem The subsystem to execute this command on.
     */
    @JsonCreator
    public ToggleShooting(@NotNull @JsonProperty(required = true) SubsystemFlywheel subsystem) {
        super(() -> {
                    switch (subsystem.getFlywheelState()) {
                        case OFF:
                            // Interrupt the command if the flywheel enters the OFF state because
                            // that implies that some other command has turned it off.
                            // But set the flywheel to SPINNING_UP before that because the command seems to sometimes
                            // be interrupted before it gets a chance to do so itself.
                            subsystem.setFlywheelState(SubsystemFlywheel.FlywheelState.SPINNING_UP);
                            new SpinUpThenShoot(subsystem).withInterrupt(
                                    () -> subsystem.getFlywheelState() == SubsystemFlywheel.FlywheelState.OFF).schedule();
                            break;
                        case SHOOTING:
                        case SPINNING_UP:
                            new TurnAllOff(subsystem).schedule();
                            break;
                        default:
                            throw new RuntimeException("Switch statement fall-through on enum type " + SubsystemFlywheel.FlywheelState.class.getSimpleName());
                    }
                },
                subsystem);
    }
}
