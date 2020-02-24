package org.usfirst.frc.team449.robot;

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
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.other.Mapping;

import java.nio.file.Path;
import java.util.Optional;

/**
 * The main class of the robot, constructs all the subsystems and initializes default commands.
 */
public class Robot extends TimedRobot {
  /**
   * The absolute filepath to the resources folder containing the config files when the robot is
   * real.
   */
  @NotNull
  public static final Path RESOURCES_PATH_REAL = Filesystem.getDeployDirectory().toPath();
  /**
   * The relative filepath to the resources folder containing the config files when the robot is
   * simulated.
   */
  @NotNull
  public static final Path RESOURCES_PATH_SIMULATED = Path.of("./src/main/deploy/");
  /**
   * The name of the map to read from.
   */
  @NotNull
  public static final Path MAP_NAME = Path.of("map.yml");
  /**
   * The filepath to the resources folder containing the config files. Either {@link
   * Robot#RESOURCES_PATH_REAL} or {@link Robot#RESOURCES_PATH_SIMULATED}.
   */
  @NotNull
  public static final Path RESOURCES_PATH = RobotBase.isReal() ? RESOURCES_PATH_REAL : RESOURCES_PATH_SIMULATED;
  /**
   * The path to the map.
   */
  @NotNull
  public static final Path MAP_PATH = RESOURCES_PATH.resolve(MAP_NAME);
  /**
   * The object constructed directly from the yaml map.
   */
  @NotNull
  protected final RobotMap robotMap;

  public Robot() {
    final Optional<RobotMap> deserializationResult = Mapping.loadMap(RobotMap.class, MAP_PATH);

    //Prevent watchdog from restarting by looping infinitely if the map could not be loaded.
    if (deserializationResult.isEmpty()) {
      //noinspection InfiniteLoopStatement
      while (true) {
        Thread.onSpinWait();
      }
    }

    robotMap = deserializationResult.get();
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
    //Read sensors
    this.robotMap.getUpdater().run();
    //update shuffleboard
    Logger.updateEntries();
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

  /**
   * Whether robot code is being unit tested. Note that this is NOT the same as test mode.
   * <p>
   * The return value will never change observably. {@link Robot#notifyTesting()} will thus throw an
   * exception if it is called after the first time that this method is called.
   * </p>
   *
   * @return whether the current run is a unit test
   */
  public static boolean isUnitTesting() {
    isTestingHasBeenCalled = true;
    return isUnitTesting;
  }

  private static boolean isUnitTesting = false;
  private static boolean isTestingHasBeenCalled = false;

  /**
   * Notifies robot code that it is being unit tested.
   *
   * @throws UnsupportedOperationException if the robot is not running in a simulation
   * @throws IllegalStateException if {@link Robot#isUnitTesting()} has already been called before
   * this method is called
   */
  public static void notifyTesting() throws UnsupportedOperationException, IllegalStateException {
    if (RobotBase.isReal())
      throw new IllegalStateException("Attempt to enable unit testing mode while not running in simulation");
    if (isTestingHasBeenCalled)
      throw new IllegalStateException("isTesting() has already been called at least once");

    System.out.println("ROBOT UNIT TESTING");
    isUnitTesting = true;
  }
}
