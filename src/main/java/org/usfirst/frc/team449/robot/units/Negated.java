package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The negation of the specified value.
 *
 * @param <U>
 */
public class Negated<
    U extends NormalizedUnitValue<U>>
    extends CompoundUnitValueBase<Negated<U>> {
  private final U value;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public Negated(final U value) {
    this.value = value;
  }

  @Override
  public U toNormalForm() {
    return this.value;
  }

  @Override
  public double getNormalizedValue() {
    return -this.value.getNormalizedValue();
  }

  @Override
  public double getRawValue() {
    return -this.value.getRawValue();
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
  public Negated<U> withValue(final double value) {
    return new Negated<>(this.value.withValue(value));
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("-%s", this.valueOfConsideringPrecedence(this.value));
  }

  @Override
  protected int getPrecedence() {
    return 10;
  }
}
