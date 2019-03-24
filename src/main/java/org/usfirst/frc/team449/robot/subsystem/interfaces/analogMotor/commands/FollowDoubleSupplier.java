package org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.oi.throttles.Throttle;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.SubsystemAnalogMotor;

import java.util.function.DoubleSupplier;

/**
 * A command to control an analog motor with a doubleSupplier.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FollowDoubleSupplier<T extends Subsystem & SubsystemAnalogMotor> extends Command {

    /**
     * The subsystem to execute this command on
     */
    @NotNull
    private final T subsystem;

    /**
     * The doubleSupplier that controls the motor.
     */
    @NotNull
    private final DoubleSupplier doubleSupplier;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     * @param doubleSupplier  The doubleSupplier that controls the motor.
     */
    @JsonCreator
    public FollowDoubleSupplier(@NotNull @JsonProperty(required = true) T subsystem,
                                @NotNull @JsonProperty(required = true) DoubleSupplier doubleSupplier) {
        this.subsystem = subsystem;
        requires(subsystem);
        this.doubleSupplier = doubleSupplier;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Logger.addEvent("FollowDoubleSupplier init", this.getClass());
    }

    /**
     * Set the motor output to the doubleSupplier output.
     */
    @Override
    protected void execute() {
        subsystem.set(doubleSupplier.getAsDouble());
    }

    /**
     * Stop when finished.
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
        Logger.addEvent("FollowDoubleSupplier end", this.getClass());
    }

    /**
     * Log that the command has been interrupted.
     */
    @Override
    protected void interrupted() {
        Logger.addEvent("FollowDoubleSupplier interrupted!", this.getClass());
    }

}
