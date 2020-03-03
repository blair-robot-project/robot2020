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
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentObserver;
import org.usfirst.frc.team449.robot.other.Clock;

/**
 * Feeder indexing. Turns the feeder on when incoming balls from the intake are detected by two
 * sensors.
 */
public class DefaultFeederCommand extends CommandBase implements Loggable {
  @NotNull private final SubsystemIntake feeder;
  @NotNull private final SubsystemIntake.IntakeMode runMode;
  @NotNull private final ConditionTimingComponentObserver feederIsOn;
  @Log.Exclude  // TODO Figure out why this is necessary to prevent logging duplicate members
  @NotNull private final ConditionTimingComponentDecorator sensor1;
  @Log.Exclude
  @NotNull private final ConditionTimingComponentDecorator sensor2;
  private final double timeout;

  /**
   * Default constructor
   *
   * @param subsystem the feeder subsystem to operate on
   * @param sensor1 the first sensor of the transition from intake to feeder
   * @param sensor2 the second sensor of the transition from intake to feeder
   * @param runMode the {@link org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake.IntakeMode}
   * to run the feeder at when
   * @param timeout maximum duration for which to keep running the feeder if the sensors remain
   * continuously activated
   */
  @JsonCreator
  public DefaultFeederCommand(
      @NotNull @JsonProperty(required = true) final SubsystemIntake subsystem,
      @NotNull @JsonProperty(required = true) final BooleanSupplier sensor1,
      @NotNull @JsonProperty(required = true) final BooleanSupplier sensor2,
      @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode runMode,
      @Deprecated final double timeout) {
    this.feeder = subsystem;
    this.feederIsOn = new ConditionTimingComponentObserver(false);
    this.sensor1 = new ConditionTimingComponentDecorator(sensor1, false);
    this.sensor2 = new ConditionTimingComponentDecorator(sensor2, false);
    this.runMode = runMode;
    this.timeout = timeout;
  }

  @Override
  public void execute() {
    final double currentTime = Clock.currentTimeSeconds();

    this.feederIsOn.update(currentTime, this.feeder.getMode() != SubsystemIntake.IntakeMode.OFF);
    this.sensor1.update(currentTime);
    this.sensor2.update(currentTime);

    this.feeder.setMode(this.shouldBeRunning() ? this.runMode : SubsystemIntake.IntakeMode.OFF);
  }

  public boolean shouldBeRunning() {
    // Give up if it's been long enough after either sensor last activated and there's still something
    // activating one of them. This specifically will continue giving up even if one of the sensors
    // deactivates but the other still surpasses the timeout.
    if (Math.min(this.sensor1.timeSinceLastBecameTrue(), this.sensor2.timeSinceLastBecameTrue()) > this.timeout) {
      return false;
    }

    // Run when either sensor is being actively tripped.
    return this.sensor1.isTrue() || this.sensor2.isTrue();
  }
}
