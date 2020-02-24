package org.usfirst.frc.team449.robot.units;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A value with units.
 *
 * @param <USelf> Curiously recurring type parameter. Used to specify the specific unit and the type
 * returned by the prototype constructor.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public abstract class NormalizedUnitValue<USelf> {
  /**
   * Gets an instance that represents the value that this unit is multiplied by to convert to the SI
   * base unit for its dimension.
   * <p>
   * Returned object may be more derived than {@link USelf} and is not guaranteed to always be the
   * same instance.
   * </p>
   *
   * @return an instance whose value represents the
   */
  @NotNull
  public abstract USelf getUnit();

  /**
   * Gets the value of this instance in terms of SI base units.
   *
   * @return the value of this instance in SI base units
   */
  public abstract double getNormalizedValue();

  /**
   * Gets the value of this instance in terms of its own units. Can also be thought of as retrieving
   * the identity value for this type in terms of unit composition.
   *
   * @return the value of this instance in its units
   */
  public abstract double getRawValue();

  /**
   * Prototype constructor.
   *
   * @param value the value
   * @return a newly constructed instance of the object
   */
  @NotNull
  @Contract(pure = true)
  public abstract USelf withValue(double value);

  /**
   * Gets a string representing this instance with numbers and unit abbreviations.
   *
   * @return a string representation of the object
   */
  @Override
  @NotNull
  public abstract String toString();

  /**
   * Gets the precedence of any operator symbols associated with the string form of this unit.
   *
   * @return the precedence used to determine whether this unit or units embedded within it are
   * quoted when formatting to a string
   */
  protected abstract int getPrecedence();

  /**
   * Gets a string representation for the specified other instance for use within the string
   * representation of this instance.
   * <p>
   * Places the other instance in parentheses if its operators do not have higher precedence than
   * those of this instance.
   * </p>
   *
   * @param other the unit to be formatted
   * @return a string representation of the specified object that is surrounded by parentheses as
   * needed
   */
  @NotNull
  protected final String valueOfConsideringPrecedence(final NormalizedUnitValue other) {
    return other.getPrecedence() > this.getPrecedence() ? other.toString() : "(" + other.toString() + ")";
  }

  /**
   * Registers the existence of a unit class.
   *
   * @param clazz the type of the class to register
   */
  protected static <T extends NormalizedUnitValue<T>> void register(final Class<T> clazz, final T unitValue) {
    units.put(clazz, unitValue);
  }

  private static final BiMap<Class<? extends NormalizedUnitValue<?>>, NormalizedUnitValue<?>> units = HashBiMap.create();

  /**
   * Attempts to convert this instance to a more normal form. Behavior is implementation-defined.
   */
  public NormalizedUnitValue<?> toNormalForm() {
    return null;
  }
}
