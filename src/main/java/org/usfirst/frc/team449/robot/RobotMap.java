package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.MotorContainer;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.units.AngleUnit;
import org.usfirst.frc.team449.robot.units.DisplacementUnit;
import org.usfirst.frc.team449.robot.units.DividedUnit;
import org.usfirst.frc.team449.robot.units.Meter;
import org.usfirst.frc.team449.robot.units.ReciprocalUnit;
import org.usfirst.frc.team449.robot.units.Second;
import org.usfirst.frc.team449.robot.units.TimeUnit;
import org.usfirst.frc.team449.robot.units.TimesUnit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * The Jackson-compatible object representing the entire robot.
 */
@JsonIgnoreProperties({"CONSTANTS", "NAVIGATION"})
public class RobotMap {
    @NotNull
    @Log.Include
    private final List<Subsystem> subsystems;

    @NotNull
    @Log.Include
    private final MotorContainer motors = MotorContainer.getInstance();

//    /**
//     * The logger for recording events and telemetry data.
//     */
//    @NotNull
//    private final Logger logger;

    /**
     * A runnable that updates cached variables.
     */
    @NotNull
    private final java.lang.Runnable updater;

    @NotNull
    private final CommandContainer commands;

    @NotNull
    @Log.Include
    private final PDP pdp;

    /**
     * Whether the camera server should be run.
     */
    private final boolean useCameraServer;

    /**
     * Default constructor.
     *
     * @param subsystems      The robot's subsystems.
     * @param pdp             The PDP
     * @param updater         A runnable that updates cached variables.
     * @param commands        A container to hold all of the robot's commands.
     * @param useCameraServer Whether the camera server should be run. Defaults to false.
     */
    @JsonCreator
    public RobotMap(@NotNull @JsonInclude(content = JsonInclude.Include.NON_NULL) final List<Subsystem> subsystems,
                    @NotNull @JsonProperty(required = true) final PDP pdp,
                    @NotNull final Runnable updater,
                    @NotNull final CommandContainer commands,
                    final boolean useCameraServer,
                    final DisplacementUnit[] distances,
                    final MappedGenericTest genericTest,
                    // TODO Don't use wildcards here either
                    final TimesUnit<? extends DisplacementUnit<?>, ? extends Foot, ? extends TimeUnit<?>, ? extends Second>[] distTimes,
                    final TimesUnit<? extends DisplacementUnit<?>, ? extends Meter, ? extends ReciprocalUnit<? extends TimeUnit<?>, ? extends Second>, ? extends ReciprocalUnit<? extends TimeUnit, ? extends Second>>[] speeds,
                    final DividedUnit<? extends AngleUnit<?>, ? extends TimeUnit>[] angularSpeeds,
                    final TimesUnit<? extends AngleUnit<?>, ? extends ReciprocalUnit<? extends TimesUnit<? extends TimeUnit<?>, ? extends Second, ? extends TimeUnit<?>, ? extends Second>>, ? extends ReciprocalUnit<? extends TimesUnit<? extends TimeUnit, ? extends Second, ? extends TimeUnit, ? extends Second>>>[] angularAccelerations) {
        this.updater = updater;
        this.pdp = pdp;
        this.useCameraServer = useCameraServer;
        this.subsystems = subsystems;
        this.commands = commands;

        Stream.concat(Arrays.stream(distances),
                Stream.concat(Arrays.stream(distTimes),
                        Stream.concat(Arrays.stream(speeds),
                                Stream.concat(Arrays.stream(angularSpeeds),
                                        Arrays.stream(angularAccelerations)
                                )
                        )
                )
        ).map(u -> u.toString() + ": " + u.getNormalizedValue()).forEach(System.err::println);
    }

//    /**
//     * @return The logger for recording events and telemetry data.
//     */
//    @NotNull
//    public Logger getLogger() {
//        return logger;
//    }

    /**
     * @return The commands to be run when first enabled in autonomous mode.
     */
    @Nullable
    public Iterator<Command> getAutoStartupCommands() {
        if (this.commands.getAutoStartupCommand() == null) {
            return null;
        }
        return this.commands.getAutoStartupCommand().iterator();
    }

    /**
     * @return The commands to be run when first enabled in teleoperated mode.
     */
    @Nullable
    public Iterator<Command> getTeleopStartupCommands() {
        if (this.commands.getTeleopStartupCommand() == null) {
            return null;
        }
        return this.commands.getTeleopStartupCommand().iterator();
    }

    @Nullable
    public Iterator<Command> getTestStartupCommands() {
        if (this.commands.getTestStartupCommand() == null) {
            return null;
        }
        return this.commands.getTestStartupCommand().iterator();
    }

    /**
     * @return The commands to be run when first enabled.
     */
    @Nullable
    public Iterator<Command> getRobotStartupCommands() {
        if (this.commands.getRobotStartupCommand() == null) {
            return null;
        }
        return this.commands.getRobotStartupCommand().iterator();
    }

    /**
     * @return A runnable that updates cached variables.
     */
    @NotNull
    public java.lang.Runnable getUpdater() {
        return this.updater;
    }

    /**
     * @return Whether the camera server should be run.
     */
    public boolean useCameraServer() {
        return this.useCameraServer;
    }

}
