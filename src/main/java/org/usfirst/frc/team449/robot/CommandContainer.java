package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.oi.buttons.CommandButton;
import org.usfirst.frc.team449.robot.other.DefaultCommand;

import java.util.List;

/**
 * A container class that holds all the commands on the robot, for cleanliness in the map and so that they all appear
 * under the same tab on the dashboard.
 */
public class CommandContainer implements Loggable {

  private final List<DefaultCommand> defaultCommands;

  private final List<CommandButton> buttons;

  private final List<Command> robotStartupCommand;

  private final List<Command> autoStartupCommand;

  private final List<Command> teleopStartupCommand;

  private final List<Command> testStartupCommand;

  @JsonCreator
  public CommandContainer(@Nullable List<DefaultCommand> defaultCommands,
                          @Nullable List<CommandButton> buttons,
                          @Nullable List<Command> robotStartupCommand,
                          @Nullable List<Command> autoStartupCommand,
                          @Nullable List<Command> teleopStartupCommand,
                          @Nullable List<Command> testStartupCommand) {
    this.defaultCommands = defaultCommands;
    this.buttons = buttons;
    this.robotStartupCommand = robotStartupCommand;
    this.autoStartupCommand = autoStartupCommand;
    this.teleopStartupCommand = teleopStartupCommand;
    this.testStartupCommand = testStartupCommand;
  }

  public List<Command> getRobotStartupCommand() {
    return robotStartupCommand;
  }

  public List<Command> getAutoStartupCommand() {
    return autoStartupCommand;
  }

  public List<Command> getTeleopStartupCommand() {
    return teleopStartupCommand;
  }

  public List<Command> getTestStartupCommand(){
    return testStartupCommand;
  }

  @Override
  public String configureLogName() {
    return "Commands";
  }
}
