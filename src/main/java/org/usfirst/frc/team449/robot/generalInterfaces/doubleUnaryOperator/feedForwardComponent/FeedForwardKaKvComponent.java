package org.usfirst.frc.team449.robot.generalInterfaces.doubleUnaryOperator.feedForwardComponent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link FeedForwardComponent} for use on a drive characterized in the way described in our white paper.
 */
public class FeedForwardKaKvComponent extends FeedForwardComponent {

    /**
     * The voltage required to run forwards or backwards at a steady-state velocity of 1 foot per second.
     */
    private final double kVFwd, kVRev;

    /**
     * The voltage required to accelerate the robot at one foot per second^2 while going forwards or backwards.
     */
    private final double kAFwd, kARev;

    /**
     * The voltage required to overcome static friction in the forwards or backwards direction.
     */
    private final double interceptVoltageFwd, interceptVoltageRev;

    /**
     * The difference between the current motor position and the desired one. Field to avoid garbage collection.
     */
    private double posDifference;

    /**
     * Default constructor.
     *
     * @param kVFwd               The voltage required to run forwards at a steady-state velocity of 1 foot per second.
     * @param kVRev               The voltage required to run backwards at a steady-state velocity of 1 foot per second.
     *                            Defaults to kVFwd.
     * @param kAFwd               The voltage required to accelerate the robot at one foot per second^2 while going
     *                            forwards. Defaults to zero, meaning we don't use acceleration feed-forward.
     * @param kARev               The voltage required to accelerate the robot at one foot per second^2 while going in
     *                            reverse. Defaults to kAFwd.
     * @param interceptVoltageFwd The voltage required to overcome static friction in the forwards direction. Vintercept
     *                            in the drive characterization paper. Defaults to 0.
     * @param interceptVoltageRev The voltage required to overcome static friction in the reverse direction. Vintercept
     *                            in the drive characterization paper. Defaults to interceptVoltageFwd.
     */
    @JsonCreator
    public FeedForwardKaKvComponent(@JsonProperty(required = true) double kVFwd,
                                    @Nullable Double kVRev,
                                    double kAFwd,
                                    @Nullable Double kARev,
                                    double interceptVoltageFwd,
                                    @Nullable Double interceptVoltageRev) {
        this.kVFwd = kVFwd;
        this.kVRev = kVRev != null ? kVRev : this.kVFwd;
        this.kAFwd = kAFwd;
        this.kARev = kARev != null ? kARev : this.kAFwd;
        this.interceptVoltageFwd = interceptVoltageFwd;
        this.interceptVoltageRev = interceptVoltageRev != null ? interceptVoltageRev : this.interceptVoltageFwd;
    }

    /**
     * Calculate the voltage for a setpoint in MP mode with a position, velocity, and acceleration setpoint.
     *
     * @param positionSetpoint The desired position, in feet.
     * @param velSetpoint      The desired velocity, in feet/sec.
     * @param accelSetpoint    The desired acceleration, in feet/sec^2.
     * @return The voltage, from [-12, 12], needed to achieve that velocity and acceleration.
     */
    @Override
    public double calcMPVoltage(double positionSetpoint, double velSetpoint, double accelSetpoint) {
        if (velSetpoint > 0 || (velSetpoint == 0 && accelSetpoint > 0)) {
            return velSetpoint * kVFwd + accelSetpoint * kAFwd + interceptVoltageFwd;
        } else if (velSetpoint < 0 || (velSetpoint == 0 && accelSetpoint < 0)) {
            return velSetpoint * kVRev + accelSetpoint * kARev - interceptVoltageRev;
        } else {
            return 0;
        }
    }

    /**
     * Calculate the voltage to use as a feedforward for the given position.
     *
     * @param operand the setpoint, in feet.
     * @return the feedforward voltage to use for that input.
     */
    @Override
    public double applyAsDouble(double operand) {
        posDifference = talon.getPositionFeet() - operand;
        if (posDifference == 0) {
            return 0;
        } else if (posDifference > 0) {
            return interceptVoltageFwd;
        } else {
            return -interceptVoltageRev;
        }
    }
}
