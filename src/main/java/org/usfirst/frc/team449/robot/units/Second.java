package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI time base unit second (s).
 */
public class Second extends Time<Second> {
  public Second(final double value) {
    super(value);
  }

  public Second(final int value) {
    this((double) value);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Second withValue(final double value) {
    return new Second(value);
  }

  private static final Second UNIT = new Second(1);

  @NotNull
  @Override
  public Second getUnit() {
    return UNIT;
  }

  @Override
  public String getShortUnitName() {
    return "s";
  }

  public static Second get() {
    return UNIT;
  }
}

/**
 * Use {@link Second} instead in Java.
 */
@Deprecated
class s extends Second {
  public s(final double value) {
    super(value);
  }

  public s(final int value) {
    super(value);
  }
}
