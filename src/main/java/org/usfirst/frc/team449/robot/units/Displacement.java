package org.usfirst.frc.team449.robot.units;

/**
 * Dimension representing distance or displacement (dimension {@code L}).
 *
 * @param <USelf>
 */
public abstract class Displacement<USelf extends Displacement<USelf>> extends DimensionUnitValue<USelf, Meter> {
  public Displacement(final double value) {
    super(value);
  }

  @Override
  public Meter toNormalized() {
    return new Meter(this.getNormalizedValue());
  }

//    public static ImmutableBiMap<Class<Displacement<?>>, Class<Displacement<?>>> conversions() {
//        return CONVERSIONS;
//    }

//    private static final ImmutableBiMap<Class<Displacement<?>>, Class<Displacement<?>>> CONVERSIONS =
//            ImmutableBiMap.<Class<Displacement<?>>, Class<Displacement<?>>>builder()
//                    .build();
}
