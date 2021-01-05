package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.util.Color;
import org.jetbrains.annotations.Nullable;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LEDComponent {

  final AddressableLED LEDStrip;

  final AddressableLEDBuffer buffer;

  // todo array to hold HSV values of each LED

  @JsonCreator
  public LEDComponent(@JsonProperty(required = true) int port, @Nullable Integer LEDCount) {
    LEDStrip = new AddressableLED(port);
    buffer = new AddressableLEDBuffer(LEDCount != null ? LEDCount : 10);

    LEDStrip.setLength(buffer.getLength());
  }

  public void turnStripOn() {
    LEDStrip.start();
    Shuffleboard.addEventMarker("LED Controller", "LED's on!", EventImportance.kNormal);
  }

  public void turnStripOff() {
    LEDStrip.stop();
    Shuffleboard.addEventMarker("LED Controller", "LED's off!", EventImportance.kNormal);
  }

  public void setSpecificRangeRGB(int lowerBound, int upperBound, int r, int g, int b) {
    // TODO check if the same logic used for setSpecificRangeHSB works for RGB

    int checkedLowerBound = Math.max(lowerBound, 0);
    int checkedUpperBound;

    if (upperBound < buffer.getLength()) {
      Shuffleboard.addEventMarker(
          "LED Controller",
          "Set range larger than set strip length! defaulting to set strip length",
          EventImportance.kTrivial);
      checkedUpperBound = buffer.getLength();
    } else {
      checkedUpperBound = upperBound;
    }

    for (int i = checkedLowerBound; i < checkedUpperBound; i++) {
      buffer.setRGB(i, r, g, b);
    }
  }

  public void setStripColor(Color color) {
    setStripRGB((int) (color.red * 255), (int) (color.green * 255), (int) (color.blue * 255));
  }

  public void setStripRGB(int r, int g, int b) {
    setSpecificRangeRGB(0, buffer.getLength(), r, g, b);
  }
}
