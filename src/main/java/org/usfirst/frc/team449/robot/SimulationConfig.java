package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.RobotBase;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Mapped static class.
 */
public class SimulationConfig {
    public static final Path MAP_NAME = Path.of("simConfig.yml");
    public static final Path MAP_PATH = Robot.RESOURCES_PATH.resolve(MAP_NAME);

    private static final SimulationConfig instance = Robot.loadMap(SimulationConfig.class, MAP_PATH);

    public static SimulationConfig get() {
        return instance;
    }

    private final Settings motors;
    private final Settings joysticks;

    @JsonCreator
    public SimulationConfig(@NotNull @JsonProperty(required = true) final Settings motors,
                            @NotNull @JsonProperty(required = true) final Settings joysticks) {
        this.motors = motors;
        this.joysticks = joysticks;
    }

    public Settings joysticks() {
        return this.joysticks;
    }

    public Settings motors() {
        return this.motors;
    }

    public static class Settings {
        private final boolean alwaysSimulateAll;
        private final boolean simulateAllWhenInSimulation;
        private final boolean simulateMissing;
        private final boolean replaceSimulationWithUnimplemented;

        /**
         * When to construct doubles/fakes for classes that represent hardware devices.
         * A double is constructed if a device in question matches any of the selected criteria.
         *
         * @param alwaysSimulateAll                  Simulate all instances unconditionally.
         * @param simulateAllWhenInSimulation        Simulate all instances when {@link RobotBase#isSimulation()} is {@code true}.
         * @param simulateMissing                    Simulate instances that can be confirmed to be physically nonexistent.
         *                                           Detection of hardware existence may have implementation-dependent side effects.
         * @param replaceSimulationWithUnimplemented Whether to construct instances without implementation that require minimal system resources.
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

        public boolean simulateAll() {
            return this.alwaysSimulateAll;
        }

        public boolean simulateAllWhenInSimulation() {
            return this.simulateAllWhenInSimulation;
        }

        public boolean simulateIfMissing() {
            return this.simulateMissing;
        }

        public boolean replaceSimulationWithUnimplemented() {
            return this.replaceSimulationWithUnimplemented;
        }
    }
}
