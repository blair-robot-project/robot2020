package org.usfirst.frc.team449.robot._2020.intake;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.IntakeSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemConditional;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;

/**
 * A decorator to make an intake with a digital input.
 *
 * @deprecated Use separate instances of {@link IntakeSimple} and {@link DigitalInput} instead.
 */
@Deprecated(forRemoval = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeSensored implements Subsystem, SubsystemIntake, SubsystemConditional, Loggable {

  private final SubsystemIntake implementation;
  /** The sensor for detecting if there's something in the intake. */
  private final DigitalInput sensor;

  /** The state of the condition when {@link IntakeSensored#update()} was called. */
  private boolean cachedCondition;

  /**
   * Default constructor.
   *
   * @param implementation The intake instance to wrap.
   * @param sensor The sensor for detecting if there's something in the intake.
   */
  @JsonCreator
  public IntakeSensored(
      final SubsystemIntake implementation,
      @NotNull @JsonProperty(required = true) final MappedDigitalInput sensor) {
    this.implementation = implementation;
    this.sensor = sensor;
  }

  /** @return true if the condition is met, false otherwise */
  @Override
  public boolean isConditionTrue() {
    return sensor.get();
  }

  /** @return true if the condition was met when cached, false otherwise */
  @Override
  public boolean isConditionTrueCached() {
    return cachedCondition;
  }

  /** Updates all cached values with current ones. */
  @Override
  public void update() {
    cachedCondition = isConditionTrue();
  }

  /** @return the current mode of the intake. */
  @Override
  public @NotNull IntakeMode getMode() {
    return this.implementation.getMode();
  }

  /** @param mode The mode to switch the intake to. */
  @Override
  public void setMode(@NotNull final IntakeMode mode) {
    this.implementation.setMode(mode);
  }
}
