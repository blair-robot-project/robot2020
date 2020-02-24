package org.usfirst.frc.team449.robot.other;

import edu.wpi.first.hal.HALValue;
import edu.wpi.first.hal.SimValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class SimUtil {
  @Contract("_, _, !null, null -> fail")
  public static <T> T getWithSimHelper(final boolean useSimValue,
                                       final boolean setValueWhenNotSim,
                                       @Nullable final SimValue simValue,
                                       @NotNull final Supplier<T> regularImplementation) {
    final T result;

    if (useSimValue) {
      Objects.requireNonNull(simValue);
      //noinspection unchecked
      result = (T) unwrapHALValue(simValue.getValue());
    } else {
      result = regularImplementation.get();
      if (simValue != null && setValueWhenNotSim) simValue.setValue(makeHALValue(result));
    }

    return result;
  }

  /**
   * <p>
   * Returns enum as Integer.
   * </p>
   */
  @Nullable
  private static Object unwrapHALValue(@NotNull final HALValue value) {
    switch (value.getType()) {
      case HALValue.kUnassigned:
        throw new IllegalStateException("Value is unassigned.");
      case HALValue.kBoolean:
        return value.getBoolean();
      case HALValue.kDouble:
        return value.getDouble();
      case HALValue.kEnum:
      case HALValue.kInt:
        return (int) value.getLong();
      case HALValue.kLong:
        return value.getLong();
    }
    throw new IllegalStateException();
  }

  /**
   * <p>
   * Serializes Enum instance with ordinal value.
   * </p>
   */
  @NotNull
  private static HALValue makeHALValue(@Nullable final Object value) {
    if (value == null) return HALValue.makeUnassigned();
    if (Boolean.class.equals(value.getClass())) return HALValue.makeBoolean((Boolean) value);
    if (Double.class.equals(value.getClass())) return HALValue.makeDouble((Double) value);
    if (Integer.class.equals(value.getClass())) return HALValue.makeInt((Integer) value);
    if (Enum.class.equals(value.getClass())) return HALValue.makeEnum(((Enum<?>) value).ordinal());
    if (Long.class.equals(value.getClass())) return HALValue.makeLong((Long) value);
    throw new IllegalArgumentException("value must be Boolean, Double, Integer, Enum, or Long.");
  }
}
