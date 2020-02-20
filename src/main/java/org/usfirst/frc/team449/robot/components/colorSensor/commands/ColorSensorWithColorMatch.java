package org.usfirst.frc.team449.robot.components.colorSensor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.util.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensor;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensorMatching;
import org.usfirst.frc.team449.robot.jacksonWrappers.ImmutableColorMatch;

import java.util.Set;

/**
 * Decorates a {@link ColorSensor} to be a {@link ColorSensorMatching} using {@link ColorMatch}.
 */
public class ColorSensorWithColorMatch implements ColorSensorMatching {
    // Implementation differs significantly enough to decorate instead of extend.
    private final ColorSensor sensor;
    private final ImmutableColorMatch matcher;

    @JsonCreator
    public ColorSensorWithColorMatch(@NotNull @JsonProperty(required = true) final ColorSensor sensor,
                                     @NotNull @JsonProperty(required = true) final ImmutableColorMatch matcher) {
        this.sensor = sensor;
        this.matcher = matcher;
    }

    @Override
    @NotNull
    public Set<Color> getColors() {
        return this.matcher.getColors();
    }

    @Override
    @Nullable
    public Color get() {
        final Color rawColor = this.sensor.get();
        if (rawColor == null) return null;

        final ColorMatchResult matchResult = this.matcher.matchColor(rawColor);
        return matchResult == null ? null : matchResult.color;
    }

    @Override
    @Nullable
    public Color getClosest() {
        final Color rawColor = this.sensor.get();
        if (rawColor == null) return null;

        return this.matcher.matchClosestColor(rawColor).color;
    }

    @Override
    @Nullable
    public Color getRawColor() {
        return this.sensor.get();
    }
}
