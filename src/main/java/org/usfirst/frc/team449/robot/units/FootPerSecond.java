package org.usfirst.frc.team449.robot.units;

/**
 * Speed unit foot per second (ft/s, fps, ft·s<sup>-1</sup>).
 */
public class FootPerSecond extends Quotient<Foot, Second> {
  public FootPerSecond(final int value) {
    super(new Foot(1).withValue(value), Second.get());
  }

  public FootPerSecond(final double value) {
    super(new Foot(1).withValue(value), Second.get());
  }
}
