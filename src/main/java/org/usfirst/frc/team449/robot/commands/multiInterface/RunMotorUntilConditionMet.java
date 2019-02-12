package org.usfirst.frc.team449.robot.commands.multiInterface;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.SubsystemBinaryMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.SubsystemConditional;

/**
 * Run a BinaryMotor until a {@link SubsystemConditional}'s condition is met.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunMotorUntilConditionMet<T extends Subsystem & SubsystemBinaryMotor> extends Command {

    /**
     * The subsystem to execute this command on
     */
    @NotNull
    private final T subsystem;

    /**
     * The conditional subsystem this command uses.
     */
    @NotNull
    private final SubsystemConditional subsystemConditional;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     * @param subsystemConditional The conditional subsystem this command uses.
     */
    @JsonCreator
    public RunMotorUntilConditionMet(@NotNull @JsonProperty(required = true) T subsystem,
                                     @NotNull @JsonProperty(required = true) SubsystemConditional subsystemConditional) {
        requires(subsystem);
        this.subsystem = subsystem;
        this.subsystemConditional = subsystemConditional;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Logger.addEvent("RunMotorUntilConditionMet init", this.getClass());
    }

    /**
     * Run the motor
     */
    @Override
    protected void execute() {
        subsystem.turnMotorOn();
    }

    /**
     * Stop when the condition is met.
     *
     * @return true if the condition is met, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        return subsystemConditional.isConditionTrueCached();
    }

    /**
     * Stop the motor and log that the command has ended.
     */
    @Override
    protected void end() {
        //Stop the motor when we meet the condition.
        subsystem.turnMotorOff();
        Logger.addEvent("RunMotorUntilConditionMet end", this.getClass());
    }

    /**
     * Stop the motor and log that the command has been interrupted.
     */
    @Override
    protected void interrupted() {
        //Stop the motor if this command is interrupted.
        subsystem.turnMotorOff();
        Logger.addEvent("RunMotorUntilConditionMet interrupted, stopping climb.", this.getClass());
    }

}
