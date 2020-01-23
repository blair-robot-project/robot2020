package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;

import java.util.Iterator;
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
     * @return The commands to be run when first enabled in autonomous mode.
     */
    @Nullable
    public Iterator<Command> getAutoStartupCommands() {
        if(commands.getAutoStartupCommand() == null){
            return null;
        }
        return commands.getAutoStartupCommand().iterator();
    }

    /**
     * @return The commands to be run when first enabled in teleoperated mode.
     */
    @Nullable
    public Iterator<Command> getTeleopStartupCommands() {
        if(commands.getTeleopStartupCommand() == null){
            return null;
        }
        return commands.getTeleopStartupCommand().iterator();
    }

    @Nullable
    public Iterator<Command> getTestStartupCommands(){
        if(commands.getTestStartupCommand() == null){
            return null;
        }
        return commands.getTestStartupCommand().iterator();
    }

    /**
     * @return The commands to be run when first enabled.
     */
    @Nullable
    public Iterator<Command> getRobotStartupCommands() {
        if(commands.getRobotStartupCommand() == null){
            return null;
        }
        return commands.getRobotStartupCommand().iterator();
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
