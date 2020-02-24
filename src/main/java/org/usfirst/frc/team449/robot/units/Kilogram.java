package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI distance base unit kilogram (kg).
 */
public class Kilogram extends Displacement<Kilogram> {
  public Kilogram(final double value) {
    super(value);
  }

  public Kilogram(final int value) {
    this((double) value);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Kilogram withValue(final double value) {
    return new Kilogram(value);
  }

  @NotNull
  @Override
  public Kilogram getUnit() {
    return UNIT;
  }

  @Override
  public String getShortUnitName() {
    return "kg";
  }

  private static final Kilogram UNIT = new Kilogram(1);

  static {
    register(Kilogram.class, UNIT);
  }
}

/**
 * Use {@link Kilogram} in Java instead.
 */
class kg extends Kilogram {
  public kg(final double value) {
    super(value);
  }

  public kg(final int value) {
    super(value);
  }
}
