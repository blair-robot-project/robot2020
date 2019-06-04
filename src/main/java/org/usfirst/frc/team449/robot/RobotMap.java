package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.oi.buttons.CommandButton;
import org.usfirst.frc.team449.robot.other.DefaultCommand;
import java.util.ArrayList;
import java.util.List;

/**
 * The Jackson-compatible object representing the entire robot.
 */
public class RobotMap {

    @NotNull
    private final List<Subsystem> subsystems;

//    /**
//     * The logger for recording events and telemetry data.
//     */
//    @NotNull
//    private final Logger logger;

    /**
     * A runnable that updates cached variables.
     */
    @NotNull
    private final Runnable updater;

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
     * @param updater              A runnable that updates cached variables.
     * @param useCameraServer Whether the camera server should be run. Defaults to false.
     */
    @JsonCreator
    public RobotMap(@NotNull @JsonProperty(required = true) List<Subsystem> subsystems,
                    @NotNull @JsonProperty(required = true) PDP pdp,
                    @NotNull @JsonProperty(required = true) MappedRunnable updater,
                    @NotNull @JsonProperty(required = true) CommandContainer commands,
                    boolean useCameraServer) {
        this.updater = updater;
        this.pdp = pdp;
        this.useCameraServer = useCameraServer;
        this.subsystems = subsystems;
        this.commands = commands;
    }

//    /**
//     * @return The logger for recording events and telemetry data.
//     */
//    @NotNull
//    public Logger getLogger() {
//        return logger;
//    }

    /**
     * @return The command to be run when first enabled in autonomous mode.
     */
    @Nullable
    public Command getAutoStartupCommand() {
        return commands.getAutoStartupCommand();
    }

    /**
     * @return The command to be run when first enabled in teleoperated mode.
     */
    @Nullable
    public Command getTeleopStartupCommand() {
        return commands.getTeleopStartupCommand();
    }

    /**
     * @return The command to be run when first enabled.
     */
    @Nullable
    public Command getRobotStartupCommand() {
        return commands.getRobotStartupCommand();
    }

    /**
     * @return A runnable that updates cached variables.
     */
    @NotNull
    public Runnable getUpdater() {
        return updater;
    }

    /**
     * @return Whether the camera server should be run.
     */
    public boolean useCameraServer() {
        return useCameraServer;
    }
}
