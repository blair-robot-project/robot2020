package org.usfirst.frc.team449.robot.components.colorSensor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj.util.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Color sensor that only returns colors from a certain set of colors.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public interface ColorSensorMatching extends ColorSensor {
    /**
     * Gets the element of {@link ColorSensorMatching#getColors()} that the current sensor reading matches with most
     * closely.
     *
     * <p>
     * A {@code null} return value may indicate that there is a sensor reading but no element matches it to within an
     * implementation-defined threshold.
     * </p>
     *
     * @return the element of {@link ColorSensorMatching#getColors()} that most matches the current sensor reading
     */
    @Override
    @Nullable
    Color get();

    /**
     * Gets the colors that this sensor can read.
     *
     * @return an immutable set of the colors that {@link ColorSensorMatching#get()} for this instance can return
     */
    @NotNull
    Set<Color> getColors();

    /**
     * Gets the element of {@link ColorSensorMatching#getColors()} that the current sensor reading matches with most
     * closely.
     *
     * <p>
     * Identical in behavior to {@link ColorSensorMatching#get()} but may return a non-null value if the sensor has a
     * reading but {@link ColorSensorMatching#get()} returns {@code null} due to color matching tolerance restrictions.
     * </p>
     *
     * @return the element of {@link ColorSensorMatching#getColors()} that most matches the current sensor reading
     */
    @Nullable
    Color getClosest();

    /**
     * Gets the raw sensor reading unaffected by color matching if it is available; otherwise, returns {@code null}.
     *
     * @return a raw sensor reading, if available
     */
    @Nullable
    Color getRawColor();
}
