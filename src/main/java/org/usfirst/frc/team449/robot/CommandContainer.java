package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import edu.wpi.first.wpilibj2.command.Command;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.oi.buttons.CommandButton;
import org.usfirst.frc.team449.robot.other.DefaultCommand;

/**
 * A container class that holds all the commands on the robot, for cleanliness in the map and so
 * that they all appear under the same tab on the dashboard.
 */
public class CommandContainer implements Loggable {
  @Log.Include private final List<DefaultCommand> defaultCommands;
  @Log.Include private final List<CommandButton> buttons;

  private final @NotNull List<Command> robotStartupCommand;
  private final @NotNull List<Command> autoStartupCommand;
  private final @NotNull List<Command> teleopStartupCommand;
  private final @NotNull List<Command> testStartupCommand;

  @JsonCreator
  public CommandContainer(
      // TODO Figure out why @JsonInclude(JsonInclude.Include.NON_NULL) doesn't work.
      @NotNull @JsonSetter(contentNulls = Nulls.SKIP, nulls = Nulls.AS_EMPTY) final List<DefaultCommand> defaultCommands,
      @NotNull @JsonSetter(contentNulls = Nulls.SKIP, nulls = Nulls.AS_EMPTY) final List<CommandButton> buttons,
      @NotNull @JsonSetter(contentNulls = Nulls.SKIP, nulls = Nulls.AS_EMPTY) final List<Command> robotStartupCommand,
      @NotNull @JsonSetter(contentNulls = Nulls.SKIP, nulls = Nulls.AS_EMPTY) final List<Command> autoStartupCommand,
      @NotNull @JsonSetter(contentNulls = Nulls.SKIP, nulls = Nulls.AS_EMPTY) final List<Command> teleopStartupCommand,
      @NotNull @JsonSetter(contentNulls = Nulls.SKIP, nulls = Nulls.AS_EMPTY) final List<Command> testStartupCommand) {

    this.defaultCommands = Collections.unmodifiableList(Objects.requireNonNull(defaultCommands));
    this.buttons = Collections.unmodifiableList(Objects.requireNonNull(buttons));
    this.robotStartupCommand = Collections.unmodifiableList(Objects.requireNonNull(robotStartupCommand));
    this.autoStartupCommand = Collections.unmodifiableList(Objects.requireNonNull(autoStartupCommand));
    this.teleopStartupCommand = Collections.unmodifiableList(Objects.requireNonNull(teleopStartupCommand));
    this.testStartupCommand = Collections.unmodifiableList(Objects.requireNonNull(testStartupCommand));
  }

  public @NotNull Iterator<Command> getRobotStartupCommands() {
    return this.robotStartupCommand.iterator();
  }

  public @NotNull Iterator<Command> getAutoStartupCommands() {
    return this.autoStartupCommand.iterator();
  }

  public @NotNull Iterator<Command> getTeleopStartupCommands() {
    return this.teleopStartupCommand.iterator();
  }

  public @NotNull Iterator<Command> getTestStartupCommands() {
    return this.testStartupCommand.iterator();
  }

  @Override
  public String configureLogName() {
    return "Commands";
  }
}
