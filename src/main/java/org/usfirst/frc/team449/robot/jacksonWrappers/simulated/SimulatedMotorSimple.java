package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A simulated motor without inductance and the hysteresis it introduces.
 *
 * <p>
 * The voltage source is assumed to be ideal and resistanceless.
 * </p>
 *
 * <p>
 * Native angle unit is rotations. Other units are SI.
 * </p>
 */
public class SimulatedMotorSimple implements SimulatedMotor {
  // Default motor parameters.
  public static class DefaultConstants {
    protected DefaultConstants() {
    }

    /** (V) Nominal bus voltage; used to calculate maximum speed. */
    public static final double NOMINAL_VOLTAGE = 12;
    /** (kg * m^2) Moment of moving parts of motor. */
    public static final double MOMENT = 20;
    /** (Ohms) Used to calculate output current. */
    public static final double RESISTANCE = 1;
    /** (N*m / V) K_t - torque per volt due to force of motor. */
    public static final double K_t = 450;
    /** (N*m / (rev/s)) Torque per RPS due to motor internal friction. */
    public static final double FRICTION_COEFF = -10;
  }

  /**
   * The maximum speed that the simulation will by nature allow this motor to sustain at its nominal
   * voltage. Not really used for anything.
   */
  @Log
  private final double nominalMaxSpeed;

  // Parameters for this motor.
  protected final double moment;
  protected final double torqueCoeff;
  protected final double frictionCoeff;
  protected final double resistance;

  /** (A) Armature current. */
  protected double current;
  /** (rev/s) Signed rotations per second. */
  protected double velocity;
  /** (rev) Signed rotation value. */
  protected double position;

  /**
   * Constructs a new motor with the specified parameters.
   *
   * @param moment (kg * m^2) - moment of inertia of motor and load.
   * @param torqueConstant K_t (but also called K_m for some reason)  (N*m / A) - motor torque
   * constant in torque over armature current.
   * @param frictionCoeff (N*m / (rev/s)) - friction coefficient in torque over angular velocity
   * (should be negative)
   * @param armatureResistance R_a (Ω) - armature resistance
   */
  public SimulatedMotorSimple(@Nullable final Double moment,
                              @Nullable final Double torqueConstant,
                              @Nullable final Double frictionCoeff,
                              @Nullable final Double armatureResistance) {
    this.moment = Objects.requireNonNullElse(moment, DefaultConstants.MOMENT);
    this.torqueCoeff = Objects.requireNonNullElse(torqueConstant, DefaultConstants.K_t);
    this.frictionCoeff = Objects.requireNonNullElse(frictionCoeff, DefaultConstants.FRICTION_COEFF);
    this.resistance = Objects.requireNonNullElse(armatureResistance, DefaultConstants.RESISTANCE);

    this.nominalMaxSpeed = this.torqueCoeff * DefaultConstants.NOMINAL_VOLTAGE / -this.frictionCoeff;
  }

  /** Constructs a new motor with default parameters. */
  public SimulatedMotorSimple() {
    this(null, null, null, null);
  }

  /**
   * Updates the motor's state.
   *
   * @param deltaSecs (s) - the amount of time that has passed since the motor was last updated
   * @param voltage (V) - the current voltage applied to the motor
   * @param loadTorque T_l (N*m) - the current torque of load on the motor
   */
  @Override
  public void updatePhysics(final double deltaSecs, final double voltage, final double loadTorque) {
    final double motorTorque = this.torqueCoeff * this.current;
    final double frictionTorque = this.frictionCoeff * this.velocity;
    final double netTorque = motorTorque + frictionTorque + loadTorque;
    final double angularAcceleration = netTorque / this.moment;

    this.position += this.velocity * deltaSecs;
    this.velocity += angularAcceleration * deltaSecs;
    this.current = voltage / this.resistance;
  }

  /**
   * Gets the current currently flowing through the motor.
   *
   * @return the current current through the motor
   */
  @Override
  public double getCurrent() {
    return this.current;
  }

  /**
   * Gets the current velocity of the motor.
   *
   * @return the velocity of the motor
   */
  @Override
  public double getVelocity() {
    return this.velocity;
  }

  /**
   * Sets the velocity of the motor.
   *
   * @param value (rev/s) - the new velocity of the motor
   */
  @Override
  public void setVelocity(final double value) {
    this.velocity = value;
  }

  /**
   * Gets the current position of the motor.
   *
   * @return the position of the motor
   */
  @Override
  public double getPosition() {
    return this.position;
  }

  /**
   * Sets the position of the motor.
   *
   * @param value (rev) - the new position of the motor
   */
  @Override
  public void setPosition(final double value) {
    this.position = value;
  }
}
