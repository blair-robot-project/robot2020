package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI-allowable plane angle unit revolution (rev).
 */
public class Revolution extends Angle<Revolution> {
    public Revolution(final double value) {
        super(value);
    }

    public Revolution(final int value) {
        this((double) value);
    }

  @Contract(pure = true)
  @NotNull
  @Override
  public Revolution withValue(final double value) {
    return new Revolution(value);
  }

    private static final Revolution UNIT = new Revolution(2 * Math.PI);

  @NotNull
  @Override
  public Revolution getUnit() {
    return UNIT;
  }

    @Override
    public String getShortUnitName() {
        return "rev";
    }
}

/**
 * Use {@link Revolution} in Java instead.
 */
class rev extends Revolution {
  public rev(final double value) {
    super(value);
  }

  public rev(final int value) {
    super(value);
  }
}
