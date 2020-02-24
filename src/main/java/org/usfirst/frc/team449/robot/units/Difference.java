package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * The difference of the specified values.
 *
 * @param <U>
 */
public class Difference<
    U extends NormalizedUnitValue<U>>
    extends Sum<U> {
  private final U top;
  private final U bottom;

  @JsonCreator
  public Difference(@JsonProperty(required = true) @JsonAlias(".") final U top,
                    @JsonProperty(required = true) @JsonAlias("/") final U bottom) {
    super(top, bottom.withValue(-bottom.getRawValue()));
    this.top = top;
    this.bottom = bottom;
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s - %s", this.valueOfConsideringPrecedence(this.top), this.valueOfConsideringPrecedence(this.bottom));
  }

  @Override
  protected int getPrecedence() {
    return 0;
  }
}
