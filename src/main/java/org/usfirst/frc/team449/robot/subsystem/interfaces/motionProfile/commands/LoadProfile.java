package org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.MotionProfileData;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.SubsystemMP;

import java.util.function.Supplier;

/**
 * Loads the given profile into the subsystem, but doesn't run it.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LoadProfile<T extends Subsystem & SubsystemMP> extends InstantCommand {

    /**
     * The subsystem to execute this command on.
     */
    @NotNull
    @Log.Exclude
    private final SubsystemMP subsystem;

    /**
     * The profile to load. Can be null if profileSupplier isn't.
     */
    @Nullable
    private final MotionProfileData profile;

    /**
     * A Supplier that gets the profile to load. Ignored if profile isn't null, must be non-null if profile is null.
     */
    @Nullable
    private final Supplier<MotionProfileData> profileSupplier;

    /**
     * Default constructor
     *
     * @param subsystem The subsystem to execute this command on.
     * @param profile   The profile to load.
     */
    @JsonCreator
    public LoadProfile(@NotNull @JsonProperty(required = true) T subsystem,
                       @NotNull @JsonProperty(required = true) MotionProfileData profile) {
        this.subsystem = subsystem;
        requires(subsystem);
        this.profile = profile;
        this.profileSupplier = null;
    }

    /**
     * Constructor that takes a lambda, for use in dynamic commandGroups.
     *
     * @param subsystem       The subsystem to execute this command on.
     * @param profileSupplier A Supplier that gets the profile to load.
     */
    public LoadProfile(@NotNull SubsystemMP subsystem,
                       @NotNull Supplier<MotionProfileData> profileSupplier) {
        this.subsystem = subsystem;
        this.profileSupplier = profileSupplier;
        this.profile = null;
    }

    /**
     * Log when this command is initialized
     */
    @Override
    protected void initialize() {
        Shuffleboard.addEventMarker("LoadProfile init.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("LoadProfile init.", this.getClass());
    }

    /**
     * Load the profile.
     */
    @Override
    protected void execute() {
        if (profile != null) {
            subsystem.loadMotionProfile(profile);
        } else {
            subsystem.loadMotionProfile(profileSupplier.get());
        }
    }

    /**
     * Log when this command ends
     */
    @Override
    protected void end() {
        Shuffleboard.addEventMarker("LoadProfile end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("LoadProfile end.", this.getClass());
    }

    /**
     * Log when this command is interrupted.
     */
    @Override
    protected void interrupted() {
        Shuffleboard.addEventMarker("LoadProfile Interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("LoadProfile Interrupted!", this.getClass());
    }
}