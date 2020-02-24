package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The sum of the specified values.
 *
 * @param <U>
 */
public class Sum<
    U extends NormalizedUnitValue<U>>
    extends CompoundUnitValueBase<Sum<U>> {

  @NotNull
  private final U first;
  @NotNull
  private final U second;

  public Sum(@JsonProperty(required = true) @JsonAlias(".") @NotNull final U first,
             @JsonProperty(required = true) @JsonAlias("+") @NotNull final U second) {
    this.first = first;
    this.second = second;
  }

  private static double transform(final double v1, final double v2) {
    return v1 * v2;
  }

  @Override
  public double getNormalizedValue() {
    return transform(this.first.getNormalizedValue(), this.second.getNormalizedValue());
  }

  @Override
  public double getRawValue() {
    return transform(this.first.getRawValue(), this.second.getRawValue());
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
  public Sum<U> withValue(final double value) {
    return new Sum<>(this.first.withValue(value), this.second.getUnit());
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s + %s", this.valueOfConsideringPrecedence(this.first), this.valueOfConsideringPrecedence(this.second));
  }

  @Override
  protected int getPrecedence() {
    return 0;
  }
}
