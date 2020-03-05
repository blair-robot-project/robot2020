package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentDecorator;
import org.usfirst.frc.team449.robot.other.Clock;

/**
 * Feeder indexing. Turns the feeder on when incoming balls from the intake are detected by two
 * sensors.
 */
public class DefaultFeederCommand extends CommandBase implements Loggable {
  @NotNull private final SubsystemIntake feeder;
  @NotNull private final SubsystemIntake.IntakeMode runMode;
  @NotNull private final ConditionTimingComponentDecorator shouldBeRunning;
  @Log.Exclude  // TODO Figure out why this is necessary to prevent logging duplicate members
  @NotNull private final ConditionTimingComponentDecorator sensor;
  private final double timeout;

  /**
   * Default constructor
   *
   * @param subsystem the feeder subsystem to operate on
   * @param sensor the first sensor of the transition from intake to feeder
   * @param runMode the {@link org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake.IntakeMode}
   * to run the feeder at when
   * @param timeout maximum duration for which to keep running the feeder if the sensors remain
   * continuously activated
   */
  @JsonCreator
  public DefaultFeederCommand(
      @NotNull @JsonProperty(required = true) final SubsystemIntake subsystem,
      @NotNull @JsonProperty(required = true) final BooleanSupplier sensor,
      @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode runMode,
      @Deprecated final double timeout) {
    this.feeder = subsystem;
    this.shouldBeRunning = new ConditionTimingComponentDecorator(this::shouldBeRunning, false);
    this.sensor = new ConditionTimingComponentDecorator(sensor, false);
    this.runMode = runMode;
    this.timeout = timeout;
  }

  @Override
  public void execute() {
    final double currentTime = Clock.currentTimeSeconds();

    this.sensor.update(currentTime);

    this.shouldBeRunning.update(currentTime);
    if (this.shouldBeRunning.justBecameTrue()) this.feeder.setMode(this.runMode);
    if (this.shouldBeRunning.justBecameFalse()) this.feeder.setMode(SubsystemIntake.IntakeMode.OFF);
  }

  public boolean shouldBeRunning() {
    // Give up if it's been long enough after either sensor last activated and there's still something
    // activating one of them. This specifically will continue giving up even if one of the sensors
    // deactivates but the other still surpasses the timeout.
    if (this.sensor.timeSinceLastBecameTrue() > this.timeout) {
      return false;
    }

    // Run when either sensor is being actively tripped.
    return this.sensor.isTrue();
  }
}
