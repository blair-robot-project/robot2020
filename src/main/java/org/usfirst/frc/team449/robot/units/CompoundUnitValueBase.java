package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.NotNull;

/**
 * A value consisting of one or more other values modified or combined in some way.
 *
 * @param <USelf> Signature of inheriting class.
 */
public abstract class CompoundUnitValueBase<USelf> extends NormalizedUnitValue<USelf> {
  @NotNull
  @Override
  public USelf getUnit() {
    // Do not pull out into a constant. Overridable method call in init.
    return this.withValue(1.0);
  }
}
