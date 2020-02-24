package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A dimensionless value.
 */
public class Dimensionless extends DimensionUnitValue<Dimensionless, Dimensionless> {
  public Dimensionless(final double value) {
    super(value);
  }

  public Dimensionless(final int value) {
    this((double) value);
  }

  public static final Dimensionless UNIT = new Dimensionless(1);

  @NotNull
  @Override
  public Dimensionless getUnit() {
    return UNIT;
  }

  /**
   * Gets an object with an equivalent value to this instance in terms of SI base units.
   */
  @Override
  public Dimensionless toNormalized() {
    return this;
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Dimensionless withValue(final double value) {
    return new Dimensionless(value);
  }

  @Override
  public String getShortUnitName() {
    return "u";
  }
}
