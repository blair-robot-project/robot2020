package org.usfirst.frc.team449.robot.commands.multiInterface;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.SubsystemConditional;
import org.usfirst.frc.team449.robot.subsystem.interfaces.intake.SubsystemIntake;

/**
 * Run a BinaryMotor while a condition is true.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeUntilConditionMet<T extends Subsystem & SubsystemIntake & SubsystemConditional> extends Command {

    /**
     * The subsystem to execute this command on
     */
    @NotNull
    private final T subsystem;

    /**
     * The mode to run the intake at until the condition is met.
     */
    @NotNull
    private final SubsystemIntake.IntakeMode intakeMode;

    /**
     * The mode to run the intake at after the condition is met.
     */
    @NotNull
    private final SubsystemIntake.IntakeMode stopMode;

    /**
     * Default constructor
     *
     * @param subsystem  The subsystem to execute this command on.
     * @param intakeMode The mode to run the intake at until the condition is met.
     * @param stopMode   The mode to run the intake at after the condition is met. Defaults to off.
     */
    @JsonCreator
    public IntakeUntilConditionMet(@NotNull @JsonProperty(required = true) T subsystem,
                                   @NotNull @JsonProperty(required = true) SubsystemIntake.IntakeMode intakeMode,
                                   @Nullable SubsystemIntake.IntakeMode stopMode) {
        requires(subsystem);
        this.subsystem = subsystem;
        this.intakeMode = intakeMode;
        this.stopMode = stopMode != null ? stopMode : SubsystemIntake.IntakeMode.OFF;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("init", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    /**
     * Run the intake in the given mode
     */
    @Override
    protected void execute() {
        subsystem.setMode(intakeMode);
    }

    /**
     * Stop when the condition is met.
     *
     * @return true if the condition is met, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        return subsystem.isConditionTrueCached();
    }

    /**
     * Stop the intake and log that the command has ended.
     */
    @Override
    protected void end() {
        //Stop the intake when we meet the condition.
        subsystem.setMode(stopMode);
        Shuffleboard.addEventMarker("Command end", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    /**
     * Stop the intake and log that the command has been interrupted.
     */
    @Override
    protected void interrupted() {
        //Stop the intake if this command is interrupted.
        subsystem.setMode(SubsystemIntake.IntakeMode.OFF);
        Shuffleboard.addEventMarker("IntakeUntilConditionMet interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("IntakeUntilConditionMet interrupted!", this.getClass());
    }

}
