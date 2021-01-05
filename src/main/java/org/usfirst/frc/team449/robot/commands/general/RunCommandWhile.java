package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

/** Runs a command while a condition is true, then runs another command. */
public class RunCommandWhile extends PerpetualCommand {
  private final Command runWhile;
  private final Command runAfter;
  private final BooleanSupplier condition;

  /**
   * @param runWhile The command to be run while the condition is true.
   * @param runAfter The command to be run after the condition becomes false.
   * @param condition The condition used to determine which command runs
   * @param requiredSubsystems Required subsystems
   */
  public RunCommandWhile(
      @NotNull @JsonProperty(required = true) Command runWhile,
      @NotNull @JsonProperty(required = true) Command runAfter,
      @NotNull @JsonProperty(required = true) BooleanSupplier condition,
      @Nullable Subsystem[] requiredSubsystems) {
    super(runWhile);
    this.runWhile = runWhile;
    this.runAfter = runAfter;
    this.condition = condition;

    if (requiredSubsystems != null) this.addRequirements(requiredSubsystems);
  }

  @Override
  public void execute() {
    if (condition.getAsBoolean()) {
      super.execute();
    } else {
      runWhile.end(true);
      runAfter.initialize();
      runAfter.execute();
      while (!runAfter.isFinished())
        ;
      runAfter.end(false);
      super.end(false);
    }
  }
}
