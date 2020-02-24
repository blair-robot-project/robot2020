package org.usfirst.frc.team449.robot.units;

import edu.wpi.first.wpilibj.util.Units;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Imperial distance unit foot (ft).
 */
public class Foot extends Displacement<Foot> {
  public Foot(final double value) {
    super(value);
  }

  public Foot(final int value) {
    this((double) value);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Foot withValue(final double value) {
    return new Foot(value);
  }

  @NotNull
  @Override
  public Foot getUnit() {
    return UNIT;
  }

  @Override
  public String getShortUnitName() {
    return "ft";
  }

  public static final Foot UNIT = new Foot(Units.metersToFeet(1));

  static {
    register(Foot.class, UNIT);
  }
}

/**
 * Use {@link Foot} instead in Java.
 */
class ft extends Foot {
  public ft(final double value) {
    super(value);
  }

  public ft(final int value) {
    super(value);
  }
}
