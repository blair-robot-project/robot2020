package org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableBiMap;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensorMatching;
import org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.SubsystemAnalogMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.SubsystemConditional;

import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class _2020ControlPanelSpinner extends SubsystemBase implements SubsystemConditional, Loggable {
    /**
     * The colors of the control panel wedges in the order that they appear when spinning the control panel clockwise.
     * This is the reverse order in which they actually appear on the control panel. The control panel as viewed from
     * above is depicted below.
     *
     * <pre>
     *   Y  B
     * R      G
     * G      R
     *   B  Y</pre>
     *
     * <p>
     * As a mnemonic, the colors follow the Quattron RGBY subpixel layout twice.
     * </p>
     */
    public enum ControlPanelColor {
        RED, GREEN, BLUE, YELLOW;
    }

    private final ImmutableBiMap<ControlPanelColor, Color> colorMapping;

    @Log.Include
    private final SubsystemAnalogMotor motor;
    @NotNull
    private final ColorSensorMatching sensor;
    @Nullable
    private ControlPanelColor targetColor;

    @JsonCreator
    public _2020ControlPanelSpinner(@NotNull @JsonProperty(required = true) final SubsystemAnalogMotor motor,
                                    @NotNull @JsonProperty(required = true) final ColorSensorMatching sensor,
                                    @NotNull @JsonProperty(required = true) final Color red,
                                    @NotNull @JsonProperty(required = true) final Color green,
                                    @NotNull @JsonProperty(required = true) final Color blue,
                                    @NotNull @JsonProperty(required = true) final Color yellow) {
        this.motor = motor;
        this.sensor = sensor;

        if (!sensor.getColors().equals(Set.of(red, green, blue, yellow))) {
            throw new IllegalArgumentException("One or more provided colors does not match colors of sensor.");
        }

        this.colorMapping = ImmutableBiMap.of(
                ControlPanelColor.RED, red,
                ControlPanelColor.GREEN, green,
                ControlPanelColor.BLUE, blue,
                ControlPanelColor.YELLOW, yellow
        );
    }

    /**
     * Gets the color read by the sensor.
     *
     * @return the color read by the sensor
     */
    @Nullable
    public ControlPanelColor get() {
        return this.colorMapping.inverse().get(sensor.get());
    }

    public SubsystemAnalogMotor getMotor() {
        return this.motor;
    }

    @Nullable
    public ControlPanelColor getTargetColor() {
        return this.targetColor;
    }

    public void setTargetColor(@Nullable final ControlPanelColor targetColor) {
        this.targetColor = targetColor;
    }

    private boolean isConditionTrueCached;

    /**
     * @return true if the condition is met, false otherwise
     */
    @Override
    public boolean isConditionTrue() {
        final Color targetColor = this.colorMapping.get(this.targetColor);
        return targetColor != null && targetColor.equals(this.sensor.get());
    }

    /**
     * @return true if the condition was met when cached, false otherwise
     */
    @Override
    public boolean isConditionTrueCached() {
        return this.isConditionTrueCached;
    }

    /**
     * Updates all cached values with current ones.
     */
    @Override
    public void update() {
        this.isConditionTrueCached = this.isConditionTrue();
    }
}
