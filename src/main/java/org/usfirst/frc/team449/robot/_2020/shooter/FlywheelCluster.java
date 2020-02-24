package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemConditional;

import java.util.List;
import java.util.Optional;

/**
 * A cluster of flywheels that acts as a single flywheel. Use for systems with separate physical
 * flywheels that are controlled by separate motors.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FlywheelCluster extends SubsystemBase implements SubsystemFlywheel, Loggable {
  @NotNull
  private final List<SubsystemFlywheel> flywheels;
  @Nullable
  private final Double maximumSpeedRange;
  @NotNull
  @Log.ToString
  private FlywheelState state = FlywheelState.OFF;
  private boolean conditionMetCached;

  /**
   * @param flywheels the flywheels that make up this cluster
   * @param maximumSpeedRange threshold of range of speeds within cluster for readiness to shoot;
   * {@code null} to not impose such a requirement
   */
  @JsonCreator
  public FlywheelCluster(@NotNull @JsonProperty(required = true) final SubsystemFlywheel[] flywheels,
                         @Nullable final Double maximumSpeedRange) {
    this.flywheels = List.of(flywheels);
    this.maximumSpeedRange = maximumSpeedRange;
  }

  /**
   * Turn on each flywheel in the cluster to the speed passed to the constructor.
   */
  @Override
  public void turnFlywheelOn() {
    this.flywheels.forEach(SubsystemFlywheel::turnFlywheelOn);
  }

  /**
   * Turn each flywheel in the cluster off.
   */
  @Override
  public void turnFlywheelOff() {
    this.flywheels.forEach(SubsystemFlywheel::turnFlywheelOff);
  }

  /**
   * @return The current state of the cluster.
   */
  @Override
  public @NotNull FlywheelState getFlywheelState() {
    return this.state;
  }

  /**
   * @param state The state to switch the cluster to.
   */
  @Override
  public void setFlywheelState(@NotNull final FlywheelState state) {
    this.state = state;
    this.flywheels.forEach(x -> x.setFlywheelState(state));
  }

  /**
   * @return Longest spin-up time of any flywheel in the cluster.
   */
  @Override
  public double getSpinUpTimeSecs() {
    //noinspection OptionalGetWithoutIsPresent
    return this.flywheels.stream().mapToDouble(SubsystemFlywheel::getSpinUpTimeSecs).max().getAsDouble();
  }

  /**
   * @return Whether all flywheels in the cluster individually are ready to shoot and the speed
   * range requirement of the cluster, if active, is met.
   */
  @Override
  @Log
  public boolean isReadyToShoot() {
    if (!this.flywheels.stream().allMatch(SubsystemFlywheel::isReadyToShoot)) return false;
    return speedRangeRequirementMet();
  }

  @Log
  public boolean speedRangeRequirementMet() {
    if (this.maximumSpeedRange == null) return true;

    double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;

    for (final SubsystemFlywheel flywheel : this.flywheels) {
      final Optional<Double> speed = flywheel.getSpeed();
      if (speed.isPresent()) {
        final double value = speed.get();
        if (value > max) max = value;
        if (value < min) min = value;
      }
    }

    return max - min <= this.maximumSpeedRange;
  }

  /**
   * @return true if the condition is met, false otherwise
   */
  @Override
  public boolean isConditionTrue() {
    return this.isReadyToShoot();
  }

  /**
   * @return true if the condition was met when cached, false otherwise
   */
  @Override
  @Log
  public boolean isConditionTrueCached() {
    return this.conditionMetCached;
  }

  /**
   * Updates all cached values with current ones.
   */
  @Override
  public void update() {
    this.flywheels.forEach(SubsystemConditional::update);
    this.conditionMetCached = this.isConditionTrue();
  }
}
