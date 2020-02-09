package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import io.github.oblarg.oblog.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.Clock;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * The main class of the robot, constructs all the subsystems and initializes default commands.
 */
public class Robot extends TimedRobot {
    /**
     * The absolute filepath to the resources folder containing the config files when the robot is real.
     */
    @NotNull
    public static final String RESOURCES_PATH_REAL = Filesystem.getDeployDirectory().getAbsolutePath();
    /**
     * The relative filepath to the resources folder containing the config files when the robot is simulated.
     */
    @NotNull
    public static final String RESOURCES_PATH_SIMULATED = "./src/main/deploy/";
    /**
     * The name of the map to read from. Should be overriden by a subclass to change the name.
     */
    @NotNull
    public static final String mapName = "map.yml";
    /**
     * The filepath to the resources folder containing the config files.
     */
    @NotNull
    public static final String RESOURCES_PATH = RobotBase.isReal() ? RESOURCES_PATH_REAL : RESOURCES_PATH_SIMULATED;
    private static final FORMAT MAP_ERR_FORMAT = FORMAT.RPAD;
    /**
     * The object constructed directly from the yaml map.
     */
    @NotNull
    protected final RobotMap robotMap = Objects.requireNonNull(loadMap());

    /**
     * The method that runs when the robot is turned on. Initializes all subsystems from the map.
     */

    public static @Nullable RobotMap loadMap() {
        try {
            //Read the yaml file with SnakeYaml so we can use anchors and merge syntax.
            final Map<?, ?> normalized = (Map<?, ?>) new Yaml().load(new FileReader(RESOURCES_PATH + "/" + mapName));

            final YAMLMapper mapper = new YAMLMapper();

            //Turn the Map read by SnakeYaml into a String so Jackson can read it.
            final String fixed = mapper.writeValueAsString(normalized);

            //Use a parameter name module so we don't have to specify name for every field.
            mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

            //Add mix-ins
            mapper.registerModule(new WPIModule());
            mapper.registerModule(new JavaModule());

            //Deserialize the map into an object.
            return mapper.readValue(fixed, RobotMap.class);

        } catch (final IOException ex) {
            //The map file is either absent from the file system or improperly formatted.
            System.out.println("Config file is bad/nonexistent!");

            final var out = new StringWriter();
            ex.printStackTrace(new PrintWriter(out));
            final String[] lines = out.toString().split("[\\r\\n]");

            for (final String line : lines) {
                if (line.length() == 0) continue;

                final String SEP = "->";
                if (line.contains(SEP) | MAP_ERR_FORMAT == FORMAT.NONE) {
                    final String[] refs = line.split(SEP);
                    switch (MAP_ERR_FORMAT) {
                        case RPAD:
                            final Optional<String> longest = Arrays.stream(refs).skip(1).max(Comparator.comparingInt(String::length));

                            if (longest.isPresent()) {
                                final int maxLen = longest.get().length();
                                final String fmt = "\t\t->%s$" + maxLen + "s\n";

                                for (int i = 1; i < refs.length; i++) {
                                    System.err.format(fmt, refs[i]);
                                }
                            }
                            break;

                        case TABLE:
                            final List<List<String>> formatted = new ArrayList<>(refs.length + 1);
                            for (int i = 0; i < refs.length; i++) {
                                final String ref = refs[i];

                                final int locationBegin = ref.lastIndexOf('[');
                                final String location = ref.substring(locationBegin + 1, ref.lastIndexOf(']'));

                                String className = ref.substring(0, locationBegin);

                                if (i == 0) {
                                    final int prefixEnd = className.lastIndexOf(":");
                                    formatted.add(List.of("", className.substring(0, prefixEnd + 1)));
                                    className = className.substring(prefixEnd + 2);
                                }
                                formatted.add(List.of(String.format("\t\t-> %s", location), className));
                            }
                            System.err.print(formatTable(formatted));
                            break;
                    }
                } else {
                    System.err.println(line);
                }
            }

            //Prevent watchdog from restarting by looping infinitely, but only when on the robot in order not to hang unit tests.
            if (RobotBase.isSimulation()) return null;
            while (true) {
            }
        }
    }

    /**
     * Converts a {@code String[][]} array representation of a table to its {@code String} representation where every
     * column is the same width as the widest cell in that column.
     *
     * @param rows a {@code String[][]} array containing the data of the table stored in row-major order
     * @return a {@code String} containing the result of formatting the table
     */
    public static String formatTable(final List<List<String>> rows) {
        final int columnCount = rows.get(0).size();

        final StringBuilder sb = new StringBuilder();

        // Make each column the same width as the widest item in it.
        final int[] maxColumnWidths = new int[columnCount];
        for (final var row : rows) {
            for (int column = 0; column < columnCount; column++) {
                maxColumnWidths[column] = Math.max(maxColumnWidths[column], row.get(column).length() + 1);
            }
        }

        // Write to the StringBuilder row by row.
        for (final var row : rows) {
            for (int column = 0; column < columnCount; column++) {
                // Use the thin vertical box-drawing character.
                sb.append(String.format("%-" + maxColumnWidths[column] + "s", row.get(column)));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void robotInit() {
        //Set up start time
        Clock.setStartTime();

        //Yes this should be a print statement, it's useful to know that robotInit started.
        System.out.println("Started robotInit.");

        if (this.robotMap.useCameraServer()) {
            CameraServer.getInstance().startAutomaticCapture();
        }

        //Read sensors
        this.robotMap.getUpdater().run();

        Logger.configureLoggingAndConfig(this.robotMap, false);
        Shuffleboard.setRecordingFileNameFormat("log-${time}");
        Shuffleboard.startRecording();

        //start systems
        if (this.robotMap.getRobotStartupCommands() != null) {
            this.robotMap.getRobotStartupCommands().forEachRemaining(Command::schedule);
        }
    }

    @Override
    public void robotPeriodic() {
        //save current time
        Clock.updateTime();
        //update shuffleboard
        Logger.updateEntries();
        //Read sensors
        this.robotMap.getUpdater().run();
        //Run all commands. This is a WPILib thing you don't really have to worry about.
        CommandScheduler.getInstance().run();
    }

    /**
     * Run when we first enable in teleop.
     */
    @Override
    public void teleopInit() {
        //cancel remaining auto commands
        if (this.robotMap.getAutoStartupCommands() != null) {
            this.robotMap.getAutoStartupCommands().forEachRemaining(Command::cancel);
        }

        //Run teleop startup commands
        if (this.robotMap.getTeleopStartupCommands() != null) {
            this.robotMap.getTeleopStartupCommands().forEachRemaining(Command::schedule);
        }
    }

    /**
     * Run when we first enable in autonomous
     */
    @Override
    public void autonomousInit() {
        //Run the auto startup command
        if (this.robotMap.getAutoStartupCommands() != null && !DriverStation.getInstance().getGameSpecificMessage().isEmpty()) {
            this.robotMap.getAutoStartupCommands().forEachRemaining(Command::schedule);
        }
    }

    /**
     * Run when we first enable in test mode.
     */
    @Override
    public void testInit() {
        //Run startup command if we start in test mode.
        if (this.robotMap.getTestStartupCommands() != null) {
            this.robotMap.getTestStartupCommands().forEachRemaining(Command::schedule);
        }
    }

    private enum FORMAT {
        TABLE, RPAD, NONE;
    }
}
