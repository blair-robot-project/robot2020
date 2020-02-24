package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI distance base unit ampere (A).
 */
public class Ampere extends Displacement<Ampere> {
  public Ampere(final double value) {
    super(value);
  }

  public Ampere(final int value) {
    this((double) value);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Ampere withValue(final double value) {
    return new Ampere(value);
  }

  @NotNull
  @Override
  public Ampere getUnit() {
    return UNIT;
  }

  @Override
  public String getShortUnitName() {
    return "A";
  }

  private static final Ampere UNIT = new Ampere(1);

  static {
    register(Ampere.class, UNIT);
  }
}

/**
 * Use {@link Ampere} in Java instead.
 */
class A extends Ampere {
  public A(final double value) {
    super(value);
  }

  public A(final int value) {
    super(value);
  }
}
