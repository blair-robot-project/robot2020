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
import org.usfirst.frc.team449.robot.components.Limelight;
import org.usfirst.frc.team449.robot.other.Clock;
import org.yaml.snakeyaml.Yaml;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

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
     * The filepath to the resources folder containing the config files.
     */
    @NotNull
    public static String RESOURCES_PATH;

    /**
     * The name of the map to read from. Should be overriden by a subclass to change the name.
     */
    protected String mapName = "map.yml";

    /**
     * The object constructed directly from the yaml map.
     */
    protected RobotMap robotMap;

    /**
     * The method that runs when the robot is turned on. Initializes all subsystems from the map.
     */

    Limelight netTableGetter;
    public void robotInit() {
        //Set up start time
        Clock.setStartTime();

        //Yes this should be a print statement, it's useful to know that robotInit started.
        System.out.println("Started robotInit.");

        RESOURCES_PATH = RobotBase.isReal() ? RESOURCES_PATH_REAL : RESOURCES_PATH_SIMULATED;
        netTableGetter = new Limelight();
        Yaml yaml = new Yaml();

        try {
            //Read the yaml file with SnakeYaml so we can use anchors and merge syntax.
            Map<?, ?> normalized = (Map<?, ?>) yaml.load(new FileReader(RESOURCES_PATH + "/" + mapName));
            YAMLMapper mapper = new YAMLMapper();
            //Turn the Map read by SnakeYaml into a String so Jackson can read it.
            String fixed = mapper.writeValueAsString(normalized);
            //Use a parameter name module so we don't have to specify name for every field.
            mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
            //Add mix-ins
            mapper.registerModule(new WPIModule());
            mapper.registerModule(new JavaModule());
            //Deserialize the map into an object.
            robotMap = mapper.readValue(fixed, RobotMap.class);
        } catch (IOException e) {
            //This is either the map file not being in the file system OR it being improperly formatted.
            System.out.println("Config file is bad/nonexistent!");
            e.printStackTrace();
            //dont restart watchdog
            while(true){}
        }

        if(robotMap.useCameraServer()){
            CameraServer.getInstance().startAutomaticCapture();
        }

        this.robotMap.getUpdater().run();

        Logger.configureLoggingAndConfig(robotMap, false);
        Shuffleboard.setRecordingFileNameFormat("log-${time}");
        Shuffleboard.startRecording();

        //start systems
        if (robotMap.getRobotStartupCommands() != null) {
            robotMap.getRobotStartupCommands().forEachRemaining(Command::schedule);
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
        if (robotMap.getAutoStartupCommands() != null) {
            robotMap.getAutoStartupCommands().forEachRemaining(Command::cancel);
        }

        //Run the teleop startup command
        if (robotMap.getTeleopStartupCommands() != null) {
            robotMap.getTeleopStartupCommands().forEachRemaining(Command::schedule);
        }
    }

    /**
     * Run when we first enable in autonomous
     */
    @Override
    public void autonomousInit() {
        //Run the auto startup command
        if (robotMap.getAutoStartupCommands() != null && !DriverStation.getInstance().getGameSpecificMessage().isEmpty()) {
            robotMap.getAutoStartupCommands().forEachRemaining(Command::schedule);
        }
    }

    /**
     * Run when we first enable in test mode.
     */
    @Override
    public void testInit() {
        //Run startup command if we start in test mode.
        if(robotMap.getTestStartupCommands() != null){
            robotMap.getTestStartupCommands().forEachRemaining(Command::schedule);
        }
    }
}
