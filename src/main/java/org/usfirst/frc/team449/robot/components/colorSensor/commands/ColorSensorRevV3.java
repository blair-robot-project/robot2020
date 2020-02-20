package org.usfirst.frc.team449.robot.components.colorSensor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.components.colorSensor.ColorSensor;
import org.usfirst.frc.team449.robot.other.Util;

/**
 * Adapter for {@link ColorSensorV3} to {@link ColorSensor}.
 */
public class ColorSensorRevV3 implements ColorSensor {
  private final ColorSensorV3 sensor;

  @JsonCreator
  public ColorSensorRevV3(@JsonProperty(required = true) final ColorSensorV3 sensor) {
    this.sensor = sensor;
  }

  @NotNull
  @Override
  public Color get() {
    return this.sensor.getColor();
  }

  @Log
  private String _get() {
    return Util.valueOf(this.get());
  }
}
