package org.usfirst.frc.team449.robot.units;

// TODO: Switch everything to interfaces so this works. It doesn't pass type checking right now.

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A value in the units of whatever the SI base unit is for the dimension it is passed to. In other
 * words, {@link SIBaseUnit}.{@link SIBaseUnit#getUnit()} returns a unit of value {@literal 1}.
 */
public class SIBaseUnit extends DimensionUnitValue<SIBaseUnit, SIBaseUnit> {
  public SIBaseUnit(final double value) {
    super(value);
  }

  public SIBaseUnit(final int value) {
    this((double) value);
  }

  private static final SIBaseUnit UNIT = new SIBaseUnit(1);

  @NotNull
  @Override
  public SIBaseUnit getUnit() {
    return UNIT;
  }

  /**
   * Gets an object with an equivalent value to this instance in terms of SI base units.
   */
  @Override
  public SIBaseUnit toNormalized() {
    return this;
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
  public SIBaseUnit withValue(final double value) {
    return new SIBaseUnit(value);
  }

  @Override
  public String getShortUnitName() {
    return "u";
  }
}
