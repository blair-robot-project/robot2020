package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import org.usfirst.frc.team449.robot.SimulationConfig;
import org.usfirst.frc.team449.robot.generalInterfaces.rumbleable.Rumbleable;
import org.usfirst.frc.team449.robot.jacksonWrappers.simulated.JoystickSimulated;

import static org.usfirst.frc.team449.robot.other.Util.getLogPrefix;

/** A Jackson-compatible wrapper on a {@link Joystick}. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedJoystick extends Joystick implements Rumbleable {
  /**
   * Default constructor
   *
   * @param port The USB port of this joystick, on [0, 5].
   */
  public MappedJoystick(@JsonProperty(required = true) final int port) {
    super(port);
  }

  /**
   * Factory method to enable faking in simulation.
   *
   * @param port The USB port of this joystick, on [0, 5].
   */
  @JsonCreator
  public static MappedJoystick create(@JsonProperty(required = true) final int port) {
    final int kHIDMissing = 255;

    if (SimulationConfig.get().joysticks().simulateAll() ||
        SimulationConfig.get().joysticks().simulateAllWhenInSimulation() && RobotBase.isSimulation() ||
        DriverStation.getInstance().getJoystickType(port) == kHIDMissing) {

      System.out.println(
          getLogPrefix(MappedJoystick.class) + "Creating simulated joystick on port " + port);
      return new JoystickSimulated(port);
    }

    return new MappedJoystick(port);
  }

  /**
   * Rumble at a given strength on each side of the device.
   *
   * @param left The strength to rumble the left side, on [-1, 1]
   * @param right The strength to rumble the right side, on [-1, 1]
   */
  @Override
  public void rumble(final double left, final double right) {
    this.setRumble(RumbleType.kLeftRumble, left);
    this.setRumble(RumbleType.kRightRumble, right);
  }
}
