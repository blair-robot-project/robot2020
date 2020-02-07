package org.usfirst.frc.team449.robot.other;

import org.jetbrains.annotations.NotNull;

/**
 * Stuff that doesn't fit anywhere else
 */
public class Util {

    /**
     * Make a runnable out of a method
     * TODO log the errors instead of just throwing them
     * @param object
     * @param methodName
     * @return
     */
    public static Runnable getMethod(Object object, String methodName) {
        return () -> {
            try {
                object.getClass().getMethod(methodName).invoke(object);
            } catch (Exception e) {
                sneakyThrow(e);
            }
        };
    }

    /**
     * Do not use this
     * @param e
     * @param <T>
     * @throws T
     */
    public static <T extends Throwable> void sneakyThrow(Exception e) throws T {
        throw (T) e;
    }

    /**
     * Gets the prefix for the specified object instance when logging.
     *
     * @param o the object to retrieve a prefix for
     * @return the prefix to be prepended to the message when the specified object logs
     */
    public static String getLogPrefix(@NotNull Object o) {
        return "[" + o.getClass().getSimpleName() + "] ";
    }

    /**
     * Gets the prefix for the specified type when logging.
     *
     * @param clazz the type to retrieve a prefix for
     * @return the prefix to be prepended to the message when the specified type logs
     */
    public static String getLogPrefix(Class<?> clazz) {
        return "[" + clazz.getSimpleName() + "] ";
    }
}
