package org.usfirst.frc.team449.robot.components;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Contract;

/**
 * Utility class that records and provides methods for examining the  times at which a condition
 * becomes true or false. The class is an abstract observer to allow subclasses to provide custom
 * implementations for updating the condition. This class is unit-agnostic.
 *
 * <p>
 * Behavior may be unexpected if object is updated more frequently than values are examined.
 * </p>
 */
public abstract class ConditionTimingComponent implements Loggable {
  @Log
  private boolean current;
  @Log
  private double now = Double.NaN;
  @Log
  private double lastBecameTrue = Double.NaN;
  @Log
  private double lastBecameFalse = Double.NaN;

  public ConditionTimingComponent(final boolean initialValue) {
    this.current = initialValue;
  }

  /**
   * Updates the current state of the condition.
   *
   * @param now the current time
   * @param value the current value
   */
  @Contract(pure = false)
  protected void update(final double now, final boolean value) {
    this.now = now;
    if (value != this.current) this.forceUpdate(now, value);
  }

  /**
   * Updates the current state of the condition and acts as if condition changed even if it did
   * not.
   *
   * @param now the current time
   */
  @Contract(pure = false)
  protected void forceUpdate(final double now, final boolean value) {
    this.now = now;

    if (value) {
      this.lastBecameTrue = now;
    } else {
      this.lastBecameFalse = now;
    }

    this.current = value;
  }

  /**
   * Gets the time at which the condition last became true.
   *
   * <p>
   * Returns {@link Double#NaN} if the condition has never become true.
   * </p>
   *
   * @return the time supplied to the most recent call to {@link ConditionTimingComponent#update(double,
   * boolean)} where the value supplied to the method was true and the previous state of the
   * condition was false.
   */
  @Contract(pure = true)
  public double lastBecameTrueTime() {
    return this.lastBecameTrue;
  }

  /**
   * Gets the time at which the condition last became false.
   *
   * <p>
   * Returns {@link Double#NaN} if the condition has never become false.
   * </p>
   *
   * @return the time supplied to the most recent call to {@link ConditionTimingComponent#update(double,
   * boolean)} where the value supplied to the method was false and the previous state of the
   * condition was true.
   */
  @Contract(pure = true)
  public double lastBecameFalseTime() {
    return this.lastBecameFalse;
  }

  public double timeSinceLastBecameTrue() {
    if (!this.current) return Double.NaN;
    return this.now - this.lastBecameTrue;
  }

  public double timeSinceLastBecameFalse() {
    if (this.current) return Double.NaN;
    return this.now - this.lastBecameFalse;
  }

  /**
   * @implNote returns min(lastBecameTrue(), lastBecameFalse())
   */
  @Contract(pure = true)
  public double lastChangeTime() {
    return Math.min(this.lastBecameFalse, this.lastBecameTrue);
  }

  @Contract(pure = true)
  public boolean hasBeenFalseForAtLeast(final double duration) {
    return this.timeSinceLastBecameFalse() >= duration;
  }

  @Contract(pure = true)
  public boolean hasBeenTrueForAtLeast(final double duration) {
    return this.timeSinceLastBecameTrue() >= duration;
  }

  @Contract(pure = true)
  public boolean hasBeenFalseForAtMost(final double duration) {
    return this.timeSinceLastBecameFalse() <= duration;
  }

  @Contract(pure = true)
  public boolean hasBeenTrueForAtMost(final double duration) {
    return this.timeSinceLastBecameTrue() <= duration;
  }

  @Contract(pure = true)
  public boolean justBecameTrue() {
    return this.hasBeenTrueForAtMost(0);
  }

  @Contract(pure = true)
  public boolean justBecameFalse() {
    return this.hasBeenFalseForAtMost(0);
  }

  @Contract(pure = true)
  public boolean isTrue() {
    return this.current;
  }

  @Override
  public String configureLogName() {
    return this.toString();
  }
}

