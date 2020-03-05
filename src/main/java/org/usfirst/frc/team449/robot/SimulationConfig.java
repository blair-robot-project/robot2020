package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.RobotBase;
import java.nio.file.Path;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.Mapping;

/**
 * Represents the configuration for the simulation of various robot components.
 */
public class SimulationConfig {
  public static final Path MAP_NAME = Path.of("simConfig.yml");
  public static final Path MAP_PATH = Robot.RESOURCES_PATH.resolve(MAP_NAME);

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private static final SimulationConfig instance = Mapping.loadMap(SimulationConfig.class, MAP_PATH).get();

  public static SimulationConfig get() {
    return instance;
  }

  private final Settings motors;
  private final Settings joysticks;

  /**
   * Default constructor.
   *
   * @param motors the configuration for motors
   * @param joysticks the configuration for joysticks
   */
  @JsonCreator
  public SimulationConfig(@NotNull @JsonProperty(required = true) final Settings motors,
                          @NotNull @JsonProperty(required = true) final Settings joysticks) {
    this.motors = motors;
    this.joysticks = joysticks;
  }

  /**
   * Gets the configuration for joysticks.
   *
   * @return the configuration for the simulation of joysticks
   */
  @Contract(pure = true)
  public Settings joysticks() {
    return this.joysticks;
  }

  /**
   * Gets the configuration for motors.
   *
   * @return the configuration for the simulation of motors
   */

  @Contract(pure = true)
  public Settings motors() {
    return this.motors;
  }

  /**
   * Represents the configuration for the simulation of a specific robot component.
   */
  public static class Settings {
    private final boolean alwaysSimulateAll;
    private final boolean simulateAllWhenInSimulation;
    private final boolean simulateMissing;
    private final boolean replaceSimulationWithUnimplemented;

    /**
     * When to construct doubles/fakes for classes that represent hardware devices. A double is
     * constructed if a device in question matches any of the selected criteria.
     *
     * @param alwaysSimulateAll Simulate all instances unconditionally.
     * @param simulateAllWhenInSimulation Simulate all instances when {@link
     * RobotBase#isSimulation()} is {@code true}.
     * @param simulateMissing Simulate instances that can be proven to be physically nonexistent.
     * Detection of hardware existence may have implementation-dependent side effects.
     * @param replaceSimulationWithUnimplemented Whether to construct simulated  that require
     * minimal system resources, that is, without simulation logic.
     */
    @JsonCreator
    public Settings(@JsonProperty(required = true) final boolean alwaysSimulateAll,
                    @JsonProperty(required = true) final boolean simulateAllWhenInSimulation,
                    @JsonProperty(required = true) final boolean simulateMissing,
                    @JsonProperty(required = true) final boolean replaceSimulationWithUnimplemented) {
      this.alwaysSimulateAll = alwaysSimulateAll;
      this.simulateAllWhenInSimulation = simulateAllWhenInSimulation;
      this.simulateMissing = simulateMissing;
      this.replaceSimulationWithUnimplemented = replaceSimulationWithUnimplemented;
    }

    /**
     * Whether all instances are simulated unconditionally
     *
     * @return whether all instances are simulated unconditionally
     */
    public boolean simulateAll() {
      return this.alwaysSimulateAll;
    }

    /**
     * Whether all instances are simulated if {@link RobotBase#isSimulation()} is {@code true}.
     *
     * @return whether all instances are simulated if {@link RobotBase#isSimulation()} is {@code
     * true}
     */
    public boolean simulateAllWhenInSimulation() {
      return this.simulateAllWhenInSimulation;
    }

    /**
     * Whether all instances that can be proven to be physically nonexistent are simulated.
     * Detection of hardware existence may have implementation-dependent side effects.
     *
     * @return whether all instances that can be proven to be physically nonexistent are simulated
     */
    public boolean simulateIfMissing() {
      return this.simulateMissing;
    }

    /**
     * Whether any simulation instances that are constructed are without simulation logic.
     *
     * @return whether simulation instances are without simulation logic
     */
    public boolean replaceSimulationWithUnimplemented() {
      return this.replaceSimulationWithUnimplemented;
    }
  }
}
