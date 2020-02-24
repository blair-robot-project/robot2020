package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.NotNull;

/**
 * Raises the unit but not the value to the specified power.
 *
 * @param <U> must be direct subclass of {@link CompoundUnitValueBase}
 */
public class Quad<U extends NormalizedUnitValue<U>> extends Product<Product<Product<U, U>, U>, U> {
  private final U value;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public Quad(final U value) {
    super(new Product<>(new Product<>(value, value.getUnit()), value.getUnit()), value.getUnit());
    this.value = value;
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s^3", this.valueOfConsideringPrecedence(this.value));
  }

  @Override
  protected int getPrecedence() {
    return 2;
  }
}
