package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.Nulls;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * A mix-in for {@link edu.wpi.first.wpilibj.command.Command} that adds JsonTypeInfo and then ignores any setters. Don't
 * make sublasses of this.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public abstract class CommandMixIn {

    @JsonIgnore
    abstract void setTimeout(double seconds);

    @JsonIgnore
    abstract void setParent(CommandGroupBase parent);

    @JsonIgnore
    abstract void setInterruptible(boolean interruptible);

    @JsonIgnore
    abstract void setRunWhenDisabled(boolean run);

    @JsonSetter(value = "requirements", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
    abstract void addRequirements(Subsystem... requirements);
}
