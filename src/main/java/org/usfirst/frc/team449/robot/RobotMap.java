package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.MotorContainer;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.units.AngleUnit;
import org.usfirst.frc.team449.robot.units.DistanceUnit;
import org.usfirst.frc.team449.robot.units.NormalizedUnit;
import org.usfirst.frc.team449.robot.units.ReciprocalUnit;
import org.usfirst.frc.team449.robot.units.TimeUnit;
import org.usfirst.frc.team449.robot.units.TimesUnit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The Jackson-compatible object representing the entire robot.
 */
@JsonIgnoreProperties({"CONSTANTS", "NAVIGATION"})
public class RobotMap {

    @NotNull
    private final List<Subsystem> subsystems;

    @NotNull
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
    public RobotMap(@NotNull @JsonProperty(required = true) @JsonInclude(content = JsonInclude.Include.NON_NULL) final List<Subsystem> subsystems,
                    @NotNull @JsonProperty(required = true) final PDP pdp,
                    @NotNull @JsonProperty(required = true) final Runnable updater,
                    @NotNull @JsonProperty(required = true) final CommandContainer commands,
                    final boolean useCameraServer,
                    final DistanceUnit dist,
                    final TimesUnit<? extends DistanceUnit,? extends TimeUnit> distTime,
                    final TimesUnit<? extends DistanceUnit, ? extends ReciprocalUnit<? extends TimeUnit>>[] speeds,
                    final TimesUnit<? extends AngleUnit, ? extends ReciprocalUnit<? extends TimesUnit<? extends TimeUnit, ? extends TimeUnit>>>[] angularAccelerations) {
        this.updater = updater;
        this.pdp = pdp;
        this.useCameraServer = useCameraServer;
        this.subsystems = subsystems;
        this.commands = commands;

        System.err.println(dist.getNormalizedValue());
        System.err.println(distTime.getNormalizedValue());
        Arrays.stream(speeds).mapToDouble(NormalizedUnit::getNormalizedValue).forEach(System.err::println);
        Arrays.stream(angularAccelerations).mapToDouble(NormalizedUnit::getNormalizedValue).forEach(System.err::println);
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
