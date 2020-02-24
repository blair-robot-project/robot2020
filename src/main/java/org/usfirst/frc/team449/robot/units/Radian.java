package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI plane angle unit radian (rad).
 */
public class Radian extends Angle<Radian> {
    public Radian(final double value) {
        super(value);
    }

    public Radian(final int value) {
        this((double) value);
    }

  @Contract(pure = true)
  @NotNull
  @Override
  public Radian withValue(final double value) {
    return new Radian(value);
  }

    private static final Radian UNIT = new Radian(1);

  @NotNull
  @Override
  public Radian getUnit() {
    return UNIT;
  }

    @Override
    public String getShortUnitName() {
        return "rad";
    }
}

/**
 * Use {@link Radian} in Java instead.
 */
class rad extends Radian {
  public rad(final double value) {
    super(value);
  }

  public rad(final int value) {
    super(value);
  }
}
