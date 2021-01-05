package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * A mix-in for {@link edu.wpi.first.wpilibj2.command.CommandGroupBase} and its WPILib subclasses
 * that annotates the constructor for use with Jackson. Don't make subclasses of this.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public abstract class CommandGroupMixIn {
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public CommandGroupMixIn(@JsonProperty("commands") final Command... commands) {}

  @JsonSetter(value = "requiredSubsystems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
  @JsonAlias("requirements")
  abstract void addRequirements(Subsystem... requirements);
}
