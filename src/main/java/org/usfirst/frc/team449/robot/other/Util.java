package org.usfirst.frc.team449.robot.other;

import edu.wpi.first.wpiutil.math.MathUtil;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class.
 */
public final class Util {
    private Util() {
    }

    /**
     * Gets the prefix for the specified object instance when logging.
     *
     * @param o the object to retrieve a prefix for
     * @return the prefix to be prepended to the message when the specified object logs
     */
    public static String getLogPrefix(@NotNull final Object o) {
        return "[" + o.getClass().getSimpleName() + "] ";
    }

    /**
     * Gets the prefix for the specified type when logging.
     *
     * @param clazz the type to retrieve a prefix for
     * @return the prefix to be prepended to the message when the specified type logs
     */
    public static String getLogPrefix(final Class<?> clazz) {
        return "[" + clazz.getSimpleName() + "] ";
    }

    /**
     * Clamps the specified value to be within the specified bounds by returning the nearer bound if the value is out of range.
     *
     * @param value  the value to clamp
     * @param lBound the lower bound to clamp to
     * @param uBound the upper bound to clamp to
     * @return {@code min(max(value, lBound), uBound)}
     */
    public static double clamp(final double value, final double lBound, final double uBound) {
        if (uBound < lBound) throw new IllegalArgumentException("uBound < lBound");
        return MathUtil.clamp(value, lBound, uBound);
    }

    /**
     * Clamps the absolute value of the specified value to the interval {@code [-absBound, absBound]}
     *
     * @param value    the value to clamp
     * @param absBound the absolute bound to clamp to
     * @return {@code clamp(value, -absBound, absBound)}
     */
    public static double clamp(final double value, final double absBound) {
        return clamp(value, -absBound, absBound);
    }

    /**
     * Clamps the absolute value of the specified value to the interval {@code [-1, 1]}.
     *
     * @param value the value to clamp
     * @return {@code clamp(value, 1)}
     */
    public static double clamp(final double value) {
        return clamp(value, 1);
    }

    /**
     * Holds constants for common class or member names for uses such as providing values to {@link Log#methodName()}.
     */
    public static class WellKnownNames {
        public static final String GET_AS_DOUBLE = "getAsDouble";
    }
}
