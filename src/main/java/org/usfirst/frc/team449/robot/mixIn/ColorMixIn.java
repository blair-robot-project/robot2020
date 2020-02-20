package org.usfirst.frc.team449.robot.mixIn;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.util.Color;

/**
 * A mix-in for {@link Color} that annotates its constructor for use with Jackson.
 * Don't make subclasses of this.
 */
public abstract class ColorMixIn {
    /**
     * @see Color#Color(double, double, double)
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    // TODO Figure out why Jackson can't infer the property names here.
    public ColorMixIn(@JsonProperty(value = "red", required = true) @JsonAlias("r") final double red,
                      @JsonProperty(value = "green", required = true) @JsonAlias("g") final double green,
                      @JsonProperty(value = "blue", required = true) @JsonAlias("b") final double blue) {
    }
}
