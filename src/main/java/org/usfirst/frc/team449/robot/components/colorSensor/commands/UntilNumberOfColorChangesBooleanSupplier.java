package org.usfirst.frc.team449.robot.components.colorSensor.commands;

import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.util.Color;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.commands.general.UntilNumberOfChangesBooleanSupplier;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensor;

import java.util.function.BooleanSupplier;

/**
 * Waits for a certain number of color changes, with color being polled every time that {@link BooleanSupplier#getAsBoolean()} is called
 * and binned by a {@link ColorMatch}.
 */
public class UntilNumberOfColorChangesBooleanSupplier extends UntilNumberOfChangesBooleanSupplier<Color> {
    @NotNull
    private final ColorSensor sensor;
    @NotNull
    private final ColorMatch matcher;

    /**
     * Default constructor.
     *
     * @param sensor        sensor from which to obtain colors
     * @param matcher       matcher used to identify colors
     * @param targetChanges number of color changes to execute for
     */
    public UntilNumberOfColorChangesBooleanSupplier(@NotNull final ColorSensor sensor,
                                                    @NotNull final ColorMatch matcher,
                                                    final int targetChanges) {
        super(oldColor -> {
            final var matchResult = matcher.matchColor(sensor.get());

            // TODO decide on whether this is the best way to handle lack of a match
            return matchResult == null ? null : matchResult.color;

        }, targetChanges);

        this.sensor = sensor;
        this.matcher = matcher;
    }
}
