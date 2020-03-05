package org.usfirst.frc.team449.robot.other;

import org.jetbrains.annotations.Contract;

/**
 * A wrapper on {@link System}.currentTimeMillis that caches the time, to avoid calling the
 * currentTimeMillis method.
 */
public final class Clock {

  /** Make constructor private so it can't be called */
  private Clock() { Util.throwFromUtilityClassConstructor(); }

  /** The starting time for this clock. */
  private static long startTime;

  /** The time since startTime, in milliseconds. */
  private static long currentTime;

  /** Updates the current time. */
  public static synchronized void updateTime() {
    currentTime = System.currentTimeMillis() - startTime;
  }

  /** Sets the start time to the current time. */
  public static synchronized void setStartTime() {
    startTime = System.currentTimeMillis();
  }

  /** @return The time since the start time, in milliseconds. */
  @Contract(pure = true)
  public static synchronized long currentTimeMillis() {
    return currentTime;
  }

  /** @return The time since the start time, in esconds. */
  @Contract(pure = true)
  public static synchronized double currentTimeSeconds() {
    return currentTime * 0.001;
  }
}
