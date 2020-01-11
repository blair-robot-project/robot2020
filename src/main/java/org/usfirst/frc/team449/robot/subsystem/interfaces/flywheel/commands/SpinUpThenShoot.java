package org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.*;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.SubsystemFlywheel;

/**
 * Spin up the flywheel until it's at the target speed, then start feeding in balls.
 *
 * Does not halt the spin-up process if the flywheel is signalled to stop while this command is running.
 * Consider decorating with {@link Command#withInterrupt(java.util.function.BooleanSupplier)} with a test for flywheel state.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SpinUpThenShoot<T extends Subsystem & SubsystemFlywheel> extends SequentialCommandGroup {

    /**
     * Default constructor.
     *
     * Requires the subsystem.
     *
     * @param subsystem The subsystem to execute this command on.
     */
    @JsonCreator
    public SpinUpThenShoot(@NotNull @JsonProperty(required = true) T subsystem) {
        this.addRequirements(subsystem);
        addCommands(
                new SpinUpFlywheel(subsystem),
                new ParallelRaceGroup(
                        new WaitUntilCommand(subsystem::isAtShootingSpeed),
                        new WaitCommand(subsystem.getSpinUpTimeoutSecs())),
                new TurnAllOn(subsystem)
        );
    }
}
