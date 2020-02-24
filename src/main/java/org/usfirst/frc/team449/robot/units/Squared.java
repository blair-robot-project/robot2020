package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.NotNull;

/**
 * Squares the unit but not the value. TODO Probably need to split up toString to handle units like
 * this more cleanly (e.g. in order to put parens around the unit name only)
 *
 * @param <U> must be direct subclass of {@link CompoundUnitValueBase}
 */
public class Squared<U extends NormalizedUnitValue<U>> extends Product<U, U> {
  private final U value;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public Squared(final U value) {
    super(value, value.getUnit());
    this.value = value;
  }

  @NotNull
  @Override
  public String toString() {
    return String.format("%s^2", this.valueOfConsideringPrecedence(this.value));
  }

  @Override
  protected int getPrecedence() {
    return 2;
  }
}
