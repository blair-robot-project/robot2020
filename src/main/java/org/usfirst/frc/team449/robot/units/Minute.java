package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI-allowable time unit minute (min).
 */
public class Minute extends Time<Minute> {
  public Minute(final double value) {
    super(value);
  }

  public Minute(final int value) {
    this((double) value);
  }

  @JsonCreator
  public static Minute s_getUnit() {
    return UNIT;
  }

  @NotNull
  @Override
  public Minute getUnit() {
    return UNIT;
  }

  /**
   * Polymorphic prototype constructor.
   *
   * @param value
   * @return
   */
  @Contract(pure = true)
  @NotNull
  @Override
  public Minute withValue(final double value) {
    return new Minute(value);
  }

  @Override
  public String getShortUnitName() {
    return "min";
  }

  private static final Minute UNIT = new Minute(60);

  static {
    register(Minute.class, UNIT);
  }
}

/**
 * Use {@link Minute} in Java instead.
 */
class min extends Minute {
  public min(final double value) {
    super(value);
  }

  public min(final int value) {
    super(value);
  }
}
