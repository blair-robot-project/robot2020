package org.usfirst.frc.team449.robot.units;

/**
 * Dimension representing electrical current.
 *
 * @param <USelf> Curiously recurring type parameter. Used to specify the specific unit and the type
 * returned by the prototype constructor.
 */
public abstract class Current<USelf extends Current<USelf>> extends DimensionUnitValue<USelf, Radian> {
  public Current(final double value) {
    super(value);
  }

  @Override
  public Radian toNormalized() {
    return new Radian(this.getNormalizedValue());
  }
}
