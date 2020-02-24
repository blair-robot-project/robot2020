package org.usfirst.frc.team449.robot.units;

import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.reflection.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A value with a unit of a single dimension.
 *
 * @param <USelf> Signature of inheriting class.
 * @param <UNormal> The SI base unit for the dimensions that this unit represents
 */
public abstract class DimensionUnitValue<
    USelf extends DimensionUnitValue<USelf, UNormal>,
    UNormal extends DimensionUnitValue<UNormal, UNormal>>
    extends NormalizedUnitValue<USelf> {
  private final double value;

  public DimensionUnitValue(final double value) {
    this.value = value;
  }

  public abstract String getShortUnitName();

  @Override
  public double getNormalizedValue() {
    return this.getRawValue() * this.getUnit().getRawValue();
  }

  /**
   * Gets an object with an equivalent value to this instance in terms of SI base units.
   */
  public abstract UNormal toNormalized();

  @NotNull
  @Override
  public String toString() {
    return String.format("%s %s", this.getRawValue(), this.getShortUnitName());
  }

  @Override
  public final double getRawValue() {
    return this.value;
  }

  @Override
  protected final int getPrecedence() {
    return Integer.MAX_VALUE;
  }

  public static DimensionUnitValue<?, ?> fromUnitName(final String shortName) {
    final Stream<Class<?>> subclasses;
    subclasses = ReflectionUtil.getClasses(DimensionUnitValue.class.getPackageName()).filter(DimensionUnitValue.class::isAssignableFrom);

    return subclasses.filter(Predicate.not(Function.<Class<?>>identity().andThen(Class::getModifiers).andThen(Modifier::isAbstract)::apply)).map(clazz -> {
      try {
        return ((Class<DimensionUnitValue<?, ?>>) clazz).getConstructor(double.class).newInstance(0.0);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
        throw new RuntimeException(ex);
      }
    }).filter(((Function<DimensionUnitValue<?, ?>, String>) DimensionUnitValue::getShortUnitName).andThen(shortName::equals)::apply).findFirst().get();
  }
}
