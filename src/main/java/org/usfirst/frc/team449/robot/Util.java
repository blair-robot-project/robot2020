package org.usfirst.frc.team449.robot;

import org.jetbrains.annotations.NotNull;

public class Util {
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
     * Gets the prefix for the specified object instance when logging.
     *
     * @param clazz the type of the object to retrieve a prefix for
     * @return the prefix to be prepended to the message when the specified object logs
     */
    public static String getLogPrefix(Class<?> clazz) {
        return "[" + clazz.getSimpleName() + "] ";
    }
}
