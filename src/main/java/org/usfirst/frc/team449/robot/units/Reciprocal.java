package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The reciprocal of the specified value.
 *
 * @param <U>
 */
public class Reciprocal<
    U extends NormalizedUnitValue<U>>
    extends CompoundUnitValueBase<Reciprocal<U>> {
  private final U value;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public Reciprocal(final U value) {
    this.value = value;
  }

  @Override
  public double getNormalizedValue() {
    return 1.0 / this.value.getNormalizedValue();
  }

  @Override
  public double getRawValue() {
    return 1.0 / this.value.getRawValue();
  }

  /**
   * Polymorphic prototype constructor.
   *
   * @param value
   * @return
   */
  @NotNull
  @Override
  @Contract(pure = true)
  public Reciprocal<U> withValue(final double value) {
    return new Reciprocal<>(this.value.withValue(value));
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s^-1", this.valueOfConsideringPrecedence(this.value));
  }

  @Override
  protected int getPrecedence() {
    return 2;
  }

  @Override
  public NormalizedUnitValue<?> toNormalForm() {
    if (this.value instanceof Reciprocal<?>) {
      return ((Reciprocal<?>) this.value).value;
    }

    return super.toNormalForm();
  }
}
