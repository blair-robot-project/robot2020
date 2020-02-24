package org.usfirst.frc.team449.robot.units;

/**
 * Dimension representing duration of time (dimension {@code T}.
 *
 * @param <USelf>
 */
public abstract class Time<USelf extends Time<USelf>> extends DimensionUnitValue<USelf, Second> {
  public Time(final double normalizedValue) {
    super(normalizedValue);
  }

  @Override
  public Second toNormalized() {
    return new Second(this.getNormalizedValue());
  }
}
