package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ScaledUnit<TUnit extends NormalizedUnitValue<TUnit>> extends NormalizedUnitValue<ScaledUnit<TUnit>> {
  private final TUnit value;
  private final double factor, val, norm;

  public ScaledUnit(final TUnit value,
                    final int power) {
    this(value, Math.pow(10, power));
  }

  private ScaledUnit(final TUnit value, final double factor) {
    this.value = value;
    this.factor = factor;

    this.val = this.transform(this.value.getRawValue());
    this.norm = this.transform(this.value.getNormalizedValue());
  }

  /**
   * Gets an instance that represents the value that this unit is multiplied by to convert to the SI
   * base unit for its dimension.
   * <p>
   * Returned object may be more derived than {@link USelf} and is not guaranteed to always be the
   * same instance.
   * </p>
   */
  @NotNull
  @Override
  public ScaledUnit<TUnit> getUnit() {
    return new ScaledUnit<>(this.value.getUnit(), 1.0);
  }

  private double transform(final double v) {
    return v * this.factor;
  }

  /**
   * Gets the value of this instance in terms of SI base units.
   *
   * @return the value of this instance in SI base units
   */
  @Override
  public double getNormalizedValue() {
    return this.norm;
  }

  /**
   * Gets the value of this instance in terms of its own units.
   *
   * @return the value of this instance in its units
   */
  @Override
  public double getRawValue() {
    return this.val;
  }

  /**
   * Prototype constructor.
   *
   * @param value raw value
   * @return a newly constructed instance of the object
   */
  @Contract(pure = true)
  @NotNull
  @Override
  public ScaledUnit<TUnit> withValue(final double value) {
    return new ScaledUnit<>(this.value.withValue(value), 1.0);
  }

  /**
   * Gets a string representing this instance with numbers and unit abbreviations.
   *
   * @return a string representation of the object
   */
  @NotNull
  @Override
  public String toString() {
    return this.valueOfConsideringPrecedence(this.value);
  }

  /**
   * Gets the precedence of any operator symbols associated with the string form of this unit.
   *
   * @return the precedence used to determine whether this unit or units embedded within it are
   * quoted when formatting to a string
   */
  @Override
  protected int getPrecedence() {
    return 0;
  }
}
