package org.usfirst.frc.team449.robot.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Util {
    private Util() {
    }

    public static byte defaultIfNull(@Nullable Byte value, byte defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static short defaultIfNull(@Nullable Short value, short defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static int defaultIfNull(@Nullable Integer value, int defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static long defaultIfNull(@Nullable Long value, long defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static float defaultIfNull(@Nullable Float value, float defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static double defaultIfNull(@Nullable Double value, double defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static boolean defaultIfNull(@Nullable Boolean value, boolean defaultVal) {
        return value == null ? defaultVal : value;
    }

    public static char defaultIfNull(@Nullable Character value, char defaultVal) {
        return value == null ? defaultVal : value;
    }

    @NotNull
    public static <T> T defaultIfNull(@Nullable T value, @NotNull T defaultVal) {
        return value == null ? defaultVal : value;
    }
}
