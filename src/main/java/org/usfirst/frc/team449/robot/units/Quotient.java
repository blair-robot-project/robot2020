package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * The quotient of the specified values.
 *
 * @param <U1>
 * @param <U2>
 */
public class Quotient<
    U1 extends NormalizedUnitValue<U1>,
    U2 extends NormalizedUnitValue<U2>>
    extends Product<U1, Reciprocal<U2>> {
  private final U1 top;
  private final U2 bottom;

  // Apparently JsonCreator is only required if any of the parameters have @JsonProperty
  @JsonCreator
  public Quotient(@JsonProperty(required = true) @JsonAlias(".") final U1 top,
                  @JsonProperty(required = true) @JsonAlias("/") final U2 bottom) {
    super(top, new Reciprocal<>(bottom));
    this.top = top;
    this.bottom = bottom;
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s / %s", this.valueOfConsideringPrecedence(this.top), this.valueOfConsideringPrecedence(this.bottom));
  }

  @Override
  protected int getPrecedence() {
    return 1;
  }
}

class per<
    U1 extends NormalizedUnitValue<U1>,
    U2 extends NormalizedUnitValue<U2>> extends Quotient<U1, U2> {

  @JsonCreator
  public per(@JsonProperty(required = true) @JsonAlias(".") final U1 top,
             @JsonProperty(required = true) @JsonAlias("/") final U2 bottom) {
    super(top, bottom);
  }
}
