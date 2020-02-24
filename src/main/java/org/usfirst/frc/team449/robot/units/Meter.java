package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI distance base unit meter (m).
 */
public class Meter extends Displacement<Meter> {
  public Meter(final double value) {
    super(value);
  }

  public Meter(final int value) {
    this((double) value);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Meter withValue(final double value) {
    return new Meter(value);
  }

  @NotNull
  @Override
  public Meter getUnit() {
    return UNIT;
  }

  @Override
  public String getShortUnitName() {
    return "m";
  }

  private static final Meter UNIT = new Meter(1);

  static {
    register(Meter.class, UNIT);
  }

  @JsonCreator
  public static Meter get() {
    return UNIT;
  }
}

/**
 * Use {@link Meter} in Java instead.
 */
class m extends Meter {
  public m(final double value) {
    super(value);
  }

  public m(final int value) {
    super(value);
  }
}
