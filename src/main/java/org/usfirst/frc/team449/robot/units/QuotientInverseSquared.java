package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class QuotientInverseSquared<U1 extends NormalizedUnitValue<U1>, U2 extends NormalizedUnitValue<U2>> extends Product<U1, Reciprocal<Product<U2, U2>>> {
  private final U1 first;
  private final U2 second;

  @JsonCreator
  public QuotientInverseSquared(@JsonProperty(required = true) @JsonAlias(".") final U1 first,
                                @JsonProperty(required = true) @JsonAlias("/^2") final U2 second) {
    super(first, new Reciprocal<>(new Squared<>(second)));
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s * %s^-2", this.valueOfConsideringPrecedence(this.first), this.valueOfConsideringPrecedence(this.second));
  }
}
