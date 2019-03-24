package org.usfirst.frc.team449.robot.subsystem.interfaces.position.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.oi.throttles.Throttle;
import org.usfirst.frc.team449.robot.subsystem.interfaces.position.SubsystemPosition;

/**
 * A command to control a position subsystem with a throttle.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ControlPositionSubsystemWithThrottle<T extends Subsystem & SubsystemPosition> extends Command {

    /**
     * The subsystem to execute this command on
     */
    @NotNull
    @Log.Exclude
    private final T subsystem;

    /**
     * The throttle that controls the motor.
     */
    @NotNull
    private final Throttle throttle;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     * @param throttle  The throttle that controls the motor.
     */
    @JsonCreator
    public ControlPositionSubsystemWithThrottle(@NotNull @JsonProperty(required = true) T subsystem,
                                                @NotNull @JsonProperty(required = true) Throttle throttle) {
        this.subsystem = subsystem;
        requires(subsystem);
        this.throttle = throttle;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("ControlPositionSubsystemWithThrottle init", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("ControlPositionSubsystemWithThrottle init", this.getClass());
    }

    /**
     * Set the motor output to the throttle output.
     */
    @Override
    protected void execute() {
        subsystem.setMotorOutput(throttle.getValueCached());
    }

    /**
     * Run until interrupted.
     *
     * @return true if finished, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     * Log that the command has ended.
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("ControlPositionSubsystemWithThrottle end", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("ControlPositionSubsystemWithThrottle end", this.getClass());
    }

    /**
     * Log that the command has been interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("ControlPositionSubsystemWithThrottle interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("ControlPositionSubsystemWithThrottle interrupted!", this.getClass());
    }

}
