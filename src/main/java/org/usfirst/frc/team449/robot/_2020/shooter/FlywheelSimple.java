package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.hal.SimBoolean;
import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.annotations.Log;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemConditional;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.other.DebouncerEx;
import org.usfirst.frc.team449.robot.other.SimUtil;

/** A flywheel multiSubsystem with a single flywheel and a single-motor feeder system. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FlywheelSimple extends SubsystemBase
    implements SubsystemFlywheel, SubsystemConditional, io.github.oblarg.oblog.Loggable {

  final int SPEED_CONDITION_BUFFER_SIZE = 30;

  /** The flywheel's motor */
  @NotNull private final SmartMotor shooterMotor;

  /** Throttle at which to run the multiSubsystem, from [-1, 1] */
  private final double shooterThrottle;

  /** Time from giving the multiSubsystem voltage to being ready to fire, in seconds. */
  private final double spinUpTimeoutSecs;

  @Nullable private final Double minShootingSpeed;
  @Nullable @Log.Exclude private final SimDevice simDevice;
  @Nullable private final SimBoolean sim_manualStates, sim_isAtSpeed, sim_isTimedOut;
  @NotNull private final DebouncerEx speedConditionDebouncer = new DebouncerEx(30);
  /** Whether the flywheel is currently commanded to spin */
  @NotNull @Log.ToString private SubsystemFlywheel.FlywheelState state;
  /** Whether the condition was met last time caching was done. */
  private boolean conditionMetCached;
  @Log private double lastSpinUpTimeMS;

  /**
   * Default constructor
   *
   * @param shooterMotor The motor controlling the flywheel.
   * @param shooterThrottle The throttle, from [-1, 1], at which to run the multiSubsystem.
   * @param spinUpTimeoutSecs The amount of time that the flywheel will wait for the nominal
   *     shooting condition to be reached before signalling that it is ready to shoot regardless.
   * @param minShootingSpeed The minimum speed at which the flywheel will signal that it is ready to
   *     shoot. Null means that the flywheel will always behave according to the timeout specified
   *     by {@code spinUpTimeoutSecs}.
   */
  @JsonCreator
  public FlywheelSimple(
      @NotNull @JsonProperty(required = true) final SmartMotor shooterMotor,
      @JsonProperty(required = true) final double shooterThrottle,
      @JsonProperty(required = true) final double spinUpTimeoutSecs,
      @Nullable final Double minShootingSpeed) {

    this.shooterMotor = shooterMotor;
    this.shooterThrottle = shooterThrottle;
    this.spinUpTimeoutSecs = spinUpTimeoutSecs;
    this.minShootingSpeed = minShootingSpeed;

    this.state = FlywheelState.OFF;

    simDevice = SimDevice.create(this.getClass().getSimpleName(), shooterMotor.getPort());
    if (simDevice != null) {
      sim_manualStates = simDevice.createBoolean("ManualStates", false, false);
      sim_isAtSpeed = simDevice.createBoolean("IsAtSpeed", false, false);
      sim_isTimedOut = simDevice.createBoolean("IsTimedOut", false, false);
    } else {
      // Bless me, Father, for I have sinned.
      this.sim_manualStates = this.sim_isAtSpeed = this.sim_isTimedOut = null;
    }
  }

  /** Turn the multiSubsystem on to a map-specified speed. */
  @Override
  public void turnFlywheelOn() {
    this.shooterMotor.enable();
    this.shooterMotor.setVelocity(this.shooterThrottle);
  }

  /** Turn the multiSubsystem off. */
  @Override
  public void turnFlywheelOff() {
    this.shooterMotor.disable();
  }

  /** @return The current state of the multiSubsystem. */
  @NotNull
  @Override
  public SubsystemFlywheel.FlywheelState getFlywheelState() {
    return this.state;
  }

  /** @param state The state to switch the multiSubsystem to. */
  @Override
  public void setFlywheelState(@NotNull final SubsystemFlywheel.FlywheelState state) {
    if (this.state != FlywheelState.SPINNING_UP && state == FlywheelState.SPINNING_UP)
      this.lastSpinUpTimeMS = Clock.currentTimeMillis();
    this.state = state;
  }

  /**
   * @return Expected time from giving the multiSubsystem voltage to being ready to fire, in
   *     seconds.
   */
  @Override
  @Log
  public double getSpinUpTimeSecs() {
    return this.spinUpTimeoutSecs;
  }

  @Override
  @Log
  public boolean isReadyToShoot() {
    if (this.state == FlywheelState.OFF) return false;
    return this.speedConditionDebouncer.get() || this.spinUpHasTimedOut();
  }

  @Log
  private boolean isAtShootingSpeed() {
    return SimUtil.getWithSimHelper(
        this.sim_manualStates != null && this.sim_manualStates.get(),
        true,
        this.sim_isAtSpeed,
        () -> {
          if (this.minShootingSpeed == null) return false;

          final double actualVelocity = this.shooterMotor.getVelocity();
          // TODO: Should we be looking at velocity or speed?
          return !Double.isNaN(actualVelocity) && Math.abs(actualVelocity) >= this.minShootingSpeed;
        });
  }

  @Log
  private boolean spinUpHasTimedOut() {
    return SimUtil.getWithSimHelper(
        this.sim_manualStates != null && this.sim_manualStates.get(),
        true,
        this.sim_isTimedOut,
        () -> {
          final double timeSinceLastSpinUp = Clock.currentTimeMillis() - this.lastSpinUpTimeMS;
          return timeSinceLastSpinUp > 1000 * this.spinUpTimeoutSecs;
        });
  }

  /** @return true if the condition is met, false otherwise */
  @Override
  public boolean isConditionTrue() {
    return this.isReadyToShoot();
  }

  /** @return true if the condition was met when cached, false otherwise */
  @Override
  @Log
  public boolean isConditionTrueCached() {
    return this.conditionMetCached;
  }

  /** Updates all cached values with current ones. */
  @Override
  public void update() {
    this.speedConditionDebouncer.update(this.isAtShootingSpeed());
    this.conditionMetCached = this.isConditionTrue();
  }

  @Override
  public @NotNull Optional<Double> getSpeed() {
    return Optional.of(Math.abs(this.shooterMotor.getVelocity()));
  }

  @Override
  public String configureLogName() {
    return this.getClass().getSimpleName() + this.shooterMotor.getPort();
  }
}
