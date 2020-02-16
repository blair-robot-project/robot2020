package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * A mix-in for {@link edu.wpi.first.wpilibj2.command.InstantCommand} that annotates its constructor for use with Jackson.
 * Don't make subclasses of this.
 */
public abstract class InstantCommandMixIn {
    /**
     * @see InstantCommand#InstantCommand(java.lang.Runnable, edu.wpi.first.wpilibj2.command.Subsystem...)
     */
    @JsonCreator()
    public InstantCommandMixIn(@JsonProperty(required = true) final Runnable toRun,
                               final Subsystem... requirements) {
    }
}
