package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Immutable {@link ColorMatch} with {@link ImmutableColorMatch#getColors()}.
 */
public class ImmutableColorMatch extends ColorMatch {
    private final Set<Color> colors;

    /**
     * @param colors              set of colors to match against
     * @param confidenceThreshold the confidence interval that the most suitable match should lie within
     */
    @JsonCreator
    public ImmutableColorMatch(@NotNull @JsonProperty(required = true) final Set<Color> colors,
                               @Nullable final Integer confidenceThreshold) {
        this.colors = colors;

        for (final Color color : colors) {
            super.addColorMatch(color);
        }

        if (confidenceThreshold != null)
            super.setConfidenceThreshold(confidenceThreshold);
    }

    @Override
    @JsonIgnore
    @Contract("_ -> fail")
    public void addColorMatch(final Color color) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Immutable class");
    }

    @Override
    @JsonIgnore
    @Contract("_ -> fail")
    public void setConfidenceThreshold(final double confidence) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Immutable class");
    }

    /**
     * Gets the set of colors that can be matched
     *
     * @return
     */
    @NotNull
    public Set<Color> getColors() {
        return this.colors;
    }
}
