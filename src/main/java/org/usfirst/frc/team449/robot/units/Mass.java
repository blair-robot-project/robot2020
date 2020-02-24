package org.usfirst.frc.team449.robot.units;

/**
 * Dimension representing mass.
 *
 * @param <USelf> Curiously recurring type parameter. Used to specify the specific unit and the type
 * returned by the prototype constructor.
 */
public abstract class Mass<USelf extends Mass<USelf>> extends DimensionUnitValue<USelf, Radian> {
  public Mass(final double value) {
    super(value);
  }

  @Override
  public Radian toNormalized() {
    return new Radian(this.getNormalizedValue());
  }
}
