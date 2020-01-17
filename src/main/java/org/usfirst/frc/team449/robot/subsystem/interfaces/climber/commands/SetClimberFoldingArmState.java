package org.usfirst.frc.team449.robot.subsystem.interfaces.climber.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.climber.SubsystemClimberFoldingArm;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SetClimberFoldingArmState extends InstantCommand {

    private final SubsystemClimberFoldingArm subsystem;
    private final ClimberState state;

    @JsonCreator
    public SetClimberFoldingArmState(@NotNull @JsonProperty(required = true) SubsystemClimberFoldingArm subsystem,
                                     @NotNull @JsonProperty(required = true) ClimberState state) {
        this.subsystem = subsystem;
        this.state = state;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    public void initialize() {
        Shuffleboard.addEventMarker("SetClimberFoldingArmState init.", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    /**
     * Set the intake to the given mode.
     */
    @Override
    public void execute() {
        switch (this.state) {
            case OFF:
                this.subsystem.off();
                break;
            case RAISE:
                this.subsystem.raise();
                break;
            case LOWER:
                this.subsystem.lower();
                break;
        }
    }

    /**
     * Log when this command ends
     */
    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            Shuffleboard.addEventMarker("SetClimberFoldingArmState Interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        }
        Shuffleboard.addEventMarker("SetClimberFoldingArmState end.", this.getClass().getSimpleName(), EventImportance.kNormal);
    }

    public enum ClimberState {
        OFF, RAISE, LOWER
    }
}
