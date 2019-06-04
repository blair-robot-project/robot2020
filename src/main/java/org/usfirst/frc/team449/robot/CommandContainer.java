package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
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

  private final Command robotStartupCommand;

  private final Command autoStartupCommand;

  private final Command teleopStartupCommand;

  @JsonCreator
  public CommandContainer(@Nullable List<DefaultCommand> defaultCommands,
                          @Nullable List<CommandButton> buttons,
                          @Nullable Command robotStartupCommand,
                          @Nullable Command autoStartupCommand,
                          @Nullable Command teleopStartupCommand) {
    this.defaultCommands = defaultCommands;
    this.buttons = buttons;
    this.robotStartupCommand = robotStartupCommand != null ? robotStartupCommand : new InstantCommand();
    this.autoStartupCommand = autoStartupCommand != null ? autoStartupCommand : new InstantCommand();
    this.teleopStartupCommand = teleopStartupCommand != null ? teleopStartupCommand : new InstantCommand();
  }

  public Command getRobotStartupCommand() {
    return robotStartupCommand;
  }

  public Command getAutoStartupCommand() {
    return autoStartupCommand;
  }

  public Command getTeleopStartupCommand() {
    return teleopStartupCommand;
  }

  @Override
  public String configureLogName() {
    return "Commands";
  }
}
