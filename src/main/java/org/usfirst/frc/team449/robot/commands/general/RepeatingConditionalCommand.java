package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;
import static edu.wpi.first.wpilibj2.command.CommandGroupBase.requireUngrouped;

/**
 * A conditional command that can be run repeatedly. Runs one of
 * two commands, depending on a BooleanSupplier.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RepeatingConditionalCommand extends CommandBase {
  private final Command onTrue;
  private final Command onFalse;
  private Command selectedCommand;

  private final BooleanSupplier condition;

  /**
   * Creates a new ConditionalCommand.
   *
   * @param onTrue             the command to run if the condition is true
   * @param onFalse            the command to run if the condition is false
   * @param condition          the condition to determine which command to run
   * @param requiredSubsystems the required subsystems
   */
  public RepeatingConditionalCommand(@Nullable final Command onTrue,
                                     @Nullable final Command onFalse,
                                     @NotNull @JsonProperty(required = true) final BooleanSupplier condition,
                                     @Nullable final Subsystem[] requiredSubsystems) {
    //TODO figure out if we even need these two commands
    requireUngrouped(onTrue, onFalse);
    //CommandGroupBase.registerGroupedCommands(onTrue, onFalse);

    if (onTrue != null) {
      this.onTrue = onTrue;
      onTrue.getRequirements().forEach(this::addRequirements);
    } else {
      this.onTrue = PlaceholderCommand.getInstance();
    }
    if (onFalse != null) {
      this.onFalse = onFalse;
      onFalse.getRequirements().forEach(this::addRequirements);
    } else {
      this.onFalse = PlaceholderCommand.getInstance();
    }

    this.condition = requireNonNullParam(condition, "condition", "RepeatingConditionalCommand");

    if (requiredSubsystems != null) addRequirements(requiredSubsystems);
  }

  /**
   * Select a command according to the BooleanSupplier <code>condition</code>
   * and initialize it.
   */
  @Override
  public void initialize() {
    selectedCommand = condition.getAsBoolean() ? onTrue : onFalse;
    selectedCommand.initialize();
  }

  /**
   * Select a new command if the current one has finished, then execute the
   * selected command.
   */
  @Override
  public void execute() {
    if (selectedCommand.isFinished()) {
      selectedCommand.end(false);
      this.initialize();
    }
    selectedCommand.execute();
  }


  /**
   * End the selected command.
   */
  @Override
  public void end(boolean interrupted) {
    selectedCommand.end(interrupted);
  }


  /**
   * Whether or not the selected command is finished.
   */
  @Override
  public boolean isFinished() {
    return selectedCommand.isFinished();
  }


  /**
   * Whether or not both commands run when disabled.
   */
  @Override
  public boolean runsWhenDisabled() {
    return onTrue.runsWhenDisabled() && onFalse.runsWhenDisabled();
  }
}
