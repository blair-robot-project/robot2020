package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentDecorator;
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentObserver;
import org.usfirst.frc.team449.robot.other.Clock;

import java.util.function.BooleanSupplier;

/**
 * Feeder indexing, i.e. turning feeder on and off based on two infrared sensors.
 */
public class DefaultFeederCommand extends CommandBase implements Loggable {
  @NotNull private final SubsystemIntake feeder;
  @NotNull private final SubsystemIntake.IntakeMode runMode;
  @NotNull private final ConditionTimingComponentObserver feederIsOn;
  @Log.Exclude
  @NotNull private final ConditionTimingComponentDecorator sensor1;
  @Log.Exclude
  @NotNull private final ConditionTimingComponentDecorator sensor2;
  private final double timeout;

  /**
   * @param subsystem
   * @param sensor1
   * @param sensor2
   * @param runMode
   */
  @JsonCreator
  public DefaultFeederCommand(@NotNull @JsonProperty(required = true) final SubsystemIntake subsystem,
                              @NotNull @JsonProperty(required = true) final BooleanSupplier sensor1,
                              @NotNull @JsonProperty(required = true) final BooleanSupplier sensor2,
                              @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode runMode,
                              @JsonProperty(required = true) final double timeout) {
    this.feeder = subsystem;
    this.runMode = runMode;
    this.feederIsOn = new ConditionTimingComponentObserver(false);
    this.sensor1 = new ConditionTimingComponentDecorator(sensor1, false);
    this.sensor2 = new ConditionTimingComponentDecorator(sensor2, false);
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
    // Run when sensor is being actively tripped.
    if (this.sensor1.isTrue() || this.sensor2.isTrue()) return true;
    // Stop if timed out.
    if (this.feederIsOn.hasBeenTrueForAtLeast(this.timeout)) return false;
    // Keep running if already on and second sensor didn't just deactivate.
    return this.feederIsOn.isTrue() && !this.sensor2.justBecameFalse();
  }
}
