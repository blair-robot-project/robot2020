package org.usfirst.frc.team449.robot.units;

/**
 * Rotational speed unit revolution per minute (RPM, rev/min, rev·min<sup>-1</sup>).
 */
public class RevolutionPerMinute extends Quotient<Revolution, Minute> {
  public RevolutionPerMinute(final double value) {
    super(new Revolution(1).withValue(value), new Minute(1));
  }
}
