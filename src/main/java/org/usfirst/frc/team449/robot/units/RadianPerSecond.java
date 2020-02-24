package org.usfirst.frc.team449.robot.units;

/**
 * SI rotational speed derived unit radian per second (rad/s, rad·s<sup>-1</sup>).
 */
public class RadianPerSecond extends Quotient<Radian, Second> {
  public RadianPerSecond(final double value) {
    super(new Radian(1).withValue(value), Second.get());
  }
}
