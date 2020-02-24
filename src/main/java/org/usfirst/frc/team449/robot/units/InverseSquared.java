package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * The inverse square ({@code 1/x^2}) of the specified value.
 */
public class InverseSquared<U extends NormalizedUnitValue<U>> extends Reciprocal<Product<U, U>> {
  private final U value;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public InverseSquared(@JsonProperty(required = true) final U value) {
    super(new Squared<>(value));
    this.value = value;
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s^-2", this.valueOfConsideringPrecedence(this.value));
  }
}
