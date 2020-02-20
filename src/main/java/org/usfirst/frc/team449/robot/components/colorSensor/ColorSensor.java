package org.usfirst.frc.team449.robot.components.colorSensor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj.util.Color;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A color sensor.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public interface ColorSensor extends Supplier<Color>, Loggable {
  /**
   * Gets the color read by the sensor. Returns {@code null} if the color reading is not available
   * at the current time due to hardware or software restrictions.
   *
   * @return the color read by the sensor
   */
  @Override
  @Nullable
  Color get();
}
