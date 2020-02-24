package org.usfirst.frc.team449.robot.units;

/**
 * Dimension representing plane angle. This dimension technically does not exist under SI, as it is
 * the derived dimension L·L<sup>-1</sup>.
 *
 * @param <USelf> Curiously recurring type parameter. Used to specify the specific unit and the type
 * returned by the prototype constructor.
 */
public abstract class Angle<USelf extends Angle<USelf>> extends DimensionUnitValue<USelf, Radian> {
  public Angle(final double value) {
    super(value);
  }

  @Override
  public Radian toNormalized() {
    return new Radian(this.getNormalizedValue());
  }
}
