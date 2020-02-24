package org.usfirst.frc.team449.robot.units;

import edu.wpi.first.wpilibj.util.Units;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * SI prefixed distance unit centimeter (cm).
 */
public class Centimeter extends Displacement<Centimeter> {
  public Centimeter(final double value) {
    super(value);
  }

  public Centimeter(final int value) {
    this((double) value);
  }

  @Contract(pure = true)
  @NotNull
  @Override
  public Centimeter withValue(final double value) {
    return new Centimeter(value);
  }

  @NotNull
  @Override
  public Centimeter getUnit() {
    return new Centimeter(Units.feetToMeters(1));
  }

  @Override
  public String getShortUnitName() {
    return "ft";
  }
}

/**
 * Use {@link Centimeter} instead in Java.
 */
class cm extends Centimeter {
  public cm(final double value) {
    super(value);
  }

  public cm(final int value) {
    super(value);
  }
}
