package org.usfirst.frc.team449.robot.units;

/**
 * SI speed derived unit meter per second (m/s, m·s<sup>-1</sup>)
 */
public class MeterPerSecond extends Quotient<Meter, Second> {
  public MeterPerSecond(final double value) {
    super(Meter.get().withValue(value), Second.get());
  }
}
