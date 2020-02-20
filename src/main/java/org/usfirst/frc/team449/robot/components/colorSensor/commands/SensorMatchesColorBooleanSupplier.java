package org.usfirst.frc.team449.robot.components.colorSensor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensor;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensorMatching;

import java.util.function.BooleanSupplier;

/**
 * Checks whether the color provided by the specified sensor matches any one of or a specific one of a set of colors.
 */
public class SensorMatchesColorBooleanSupplier implements BooleanSupplier {
    @NotNull
    private final ColorMatch matcher;
    @NotNull
    private final ColorSensor sensor;
    @Nullable
    private final Color targetColor;

    /**
     * Default constructor.
     *
     * @param sensor      The sensor to obtain colors from. As a rule of thumb this argument should implement
     *                    {@link ColorSensorMatching}
     * @param targetColor The member of {@code colors} that should be the most suitable match;
     *                    if null, returns true if any color supplied to the matcher matches.
     */
    @JsonCreator
    public SensorMatchesColorBooleanSupplier(@NotNull @JsonProperty(required = true) final ColorSensor sensor,
                                             @NotNull @JsonProperty(required = true) final ColorMatch matcher,
                                             @Nullable final Color targetColor) {
        this.sensor = sensor;
        this.matcher = matcher;
        this.targetColor = targetColor;
    }

    @Override
    public boolean getAsBoolean() {
        final var actualColor = sensor.get();
        if (actualColor == null) return false;

        final var result = matcher.matchColor(actualColor);

        return result != null && (targetColor == null || targetColor.equals(result.color));
    }
}
