package org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.SubsystemFlywheel;
import org.usfirst.frc.team449.robot.subsystem.interfaces.intake.SubsystemIntake;

public class TurnOnFeederWhenAtSpeed<T extends Subsystem & SubsystemFlywheel> extends PerpetualCommand {
    @JsonCreator
    public TurnOnFeederWhenAtSpeed(@NotNull @JsonProperty(required = true) T flywheel,
                                   @Nullable SubsystemIntake feeder) {
        super(new SequentialCommandGroup(
                new WaitCommand(1),
                new ConditionalCommand(
                        new TurnAllOn(flywheel),
                        new SpinUpFlywheel(flywheel, feeder),
                        flywheel::isAtShootingSpeed)));
        this.addRequirements(flywheel);
    }
}
