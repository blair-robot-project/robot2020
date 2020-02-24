package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The product of the specified values.
 *
 * @param <U1>
 * @param <U2>
 */
public class Product<
    U1 extends NormalizedUnitValue<U1>,
    U2 extends NormalizedUnitValue<U2>>
    extends CompoundUnitValueBase<Product<U1, U2>> {

  @NotNull
  private final U1 first;
  @NotNull
  private final U2 second;

  @JsonCreator
  public Product(@JsonProperty(required = true) @JsonAlias(".") @NotNull final U1 first,
                 @JsonProperty(required = true) @JsonAlias("x") @NotNull final U2 second) {
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
  public Product<U1, U2> withValue(final double value) {
    return new Product<>(this.first.withValue(value), this.second.getUnit());
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s * %s", this.valueOfConsideringPrecedence(this.first), this.valueOfConsideringPrecedence(this.second));
  }

  @Override
  protected int getPrecedence() {
    return 1;
  }
}
