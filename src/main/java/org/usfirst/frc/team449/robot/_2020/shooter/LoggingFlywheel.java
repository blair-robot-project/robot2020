package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemConditional;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;
import org.usfirst.frc.team449.robot.other.Clock;

/**
 * A flywheel multiSubsystem with a single flywheel and a single-motor feeder system.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LoggingFlywheel extends SubsystemBase
    implements SubsystemFlywheel, SubsystemConditional, io.github.oblarg.oblog.Loggable {

  /**
   * The flywheel's Talon
   */
  @NotNull
  private final SmartMotor shooterMotor;

  @NotNull
  private final SmartMotor otherShooterMotor;

  /**
   * The feeder's motor
   */
  @NotNull
  private final SimpleMotor kickerMotor;

  /**
   * How fast to run the feeder, from [-1, 1]
   */
  private final double kickerThrottle;

  /**
   * Throttle at which to run the multiSubsystem, from [-1, 1]
   */
  private final double shooterThrottle;

  /**
   * Time from giving the multiSubsystem voltage to being ready to fire, in seconds.
   */
  private final double spinUpTimeoutSecs;

  @Nullable
  private final Double minShootingSpeed;

  /**
   * Whether the flywheel is currently commanded to spin
   */
  @NotNull
  private SubsystemFlywheel.FlywheelState state;

  /**
   * Whether the condition was met last time caching was done.
   */
  private boolean conditionMetCached;
  @Log
  private double lastSpinUpTimeMS;

  /**
   * Default constructor
   *
   * @param shooterMotor The motor controlling the flywheel.
   * @param shooterThrottle The throttle, from [-1, 1], at which to run the multiSubsystem.
   * @param kickerMotor The motor controlling the feeder.
   * @param kickerThrottle The throttle, from [-1, 1], at which to run the feeder.
   * @param spinUpTimeoutSecs The amount of time that the flywheel will wait for the nominal
   * shooting condition to be reached before signalling that it is ready to shoot regardless.
   * @param minShootingSpeed The minimum speed at which the flywheel will signal that it is ready to
   * shoot. Null means that the flywheel will always behave according to the timeout specified by
   * {@code spinUpTimeoutSecs}.
   */
  @JsonCreator
  public LoggingFlywheel(
      @NotNull @JsonProperty(required = true) final SmartMotor shooterMotor,
      @NotNull @JsonProperty(required = true) final SmartMotor otherShooterMotor,
      @JsonProperty(required = true) final double shooterThrottle,
      @NotNull @JsonProperty(required = true) final SimpleMotor kickerMotor,
      @JsonProperty(required = true) final double kickerThrottle,
      @JsonProperty(required = true) final double spinUpTimeoutSecs,
      @Nullable final Double minShootingSpeed) {
    this.shooterMotor = shooterMotor;
    this.otherShooterMotor = otherShooterMotor;
    this.shooterThrottle = shooterThrottle;
    this.kickerMotor = kickerMotor;
    this.kickerThrottle = kickerThrottle;
    this.spinUpTimeoutSecs = spinUpTimeoutSecs;
    this.minShootingSpeed = minShootingSpeed;

    this.state = FlywheelState.OFF;
  }

  /**
   * Turn the multiSubsystem on to a map-specified speed.
   */
  @Override
  public void turnFlywheelOn() {
    this.shooterMotor.enable();
    this.otherShooterMotor.enable();
    this.shooterMotor.setVelocity(this.shooterThrottle);
    this.otherShooterMotor.setVelocity(this.shooterThrottle);
  }

  /**
   * Turn the multiSubsystem off.
   */
  @Override
  public void turnFlywheelOff() {
    this.shooterMotor.disable();
    this.otherShooterMotor.disable();
  }

  /**
   * Start feeding balls into the multiSubsystem.
   */
  @Override
  public void turnFeederOn() {
    this.kickerMotor.enable();
    this.kickerMotor.setVelocity(this.kickerThrottle);
  }

  /**
   * Stop feeding balls into the multiSubsystem.
   */
  @Override
  public void turnFeederOff() {
    this.kickerMotor.disable();
  }

  /**
   * @return The current state of the multiSubsystem.
   */
  @NotNull
  @Override
  public SubsystemFlywheel.FlywheelState getFlywheelState() {
    return this.state;
  }

  /**
   * @param state The state to switch the multiSubsystem to.
   */
  @Override
  public void setFlywheelState(@NotNull final SubsystemFlywheel.FlywheelState state) {
    if (this.state != FlywheelState.SPINNING_UP && state == FlywheelState.SPINNING_UP)
      this.lastSpinUpTimeMS = Clock.currentTimeMillis();
    this.state = state;
  }

  @Log
  public String state() {
    return this.state.name();
  }

  /**
   * @return Expected time from giving the multiSubsystem voltage to being ready to fire, in
   * seconds.
   */
  @Override
  @Log
  public double getSpinUpTimeoutSecs() {
    return this.spinUpTimeoutSecs;
  }

  // TODO: Also account for speed difference between flywheels?
  // TODO: Split into FlywheelTwoSides like how intake does it?
  @Override
  @Log
  public boolean isReadyToShoot() {
    if (this.state == FlywheelState.OFF) return false;
    return this.isAtShootingSpeed() || this.spinupHasTimedOut();
  }

  @Log
  private boolean isAtShootingSpeed() {
    if (this.minShootingSpeed == null) return false;

    final Double actualVelocity = this.shooterMotor.getVelocity();
    // TODO: Should we be looking at velocity or speed?
    return !Double.isNaN(actualVelocity) && Math.abs(actualVelocity) >= this.minShootingSpeed;
  }

  @Log
  private boolean spinupHasTimedOut() {
    final double timeSinceLastSpinUp = Clock.currentTimeMillis() - this.lastSpinUpTimeMS;
    return timeSinceLastSpinUp > 1000 * this.spinUpTimeoutSecs;
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
  @
      Log
  public boolean isConditionTrueCached() {
    return this.conditionMetCached;
  }

  /**
   * Updates all cached values with current ones.
   */
  @Override
  public void update() {
    this.conditionMetCached = this.isConditionTrue();
  }
}
