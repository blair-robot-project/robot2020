package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;

/**
 * A mix-in for {@link ColorSensorV3} that annotates its constructor for use with Jackson.
 * Don't make subclasses of this.
 */
public abstract class ColorSensorV3MixIn {
    /**
     * @see ColorSensorV3#ColorSensorV3(I2C.Port)
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ColorSensorV3MixIn(@JsonProperty(required = true) final I2C.Port port) {
    }
}
