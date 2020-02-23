package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

/**
 * A simulation of a motor that provides position, velocity, and current and that can be updated .
 */
public interface SimulatedMotor extends Loggable {
    /**
     * Updates the motor's state.
     *
     * @param deltaSecs  dt  (s) - the amount of time that has passed since the motor was last updated
     * @param voltage    V_s (V) - the current voltage applied to the motor
     * @param loadTorque T_l (N*m) - the current torque of load on the motor
     */
    void updatePhysics(double deltaSecs, double voltage, double loadTorque);

    default void updatePhysics(final double deltaSecs, final double voltage) {
        this.updatePhysics(deltaSecs, voltage, 0);
    }

    /**
     * Gets the velocity of the shaft in radians per second.
     *
     * @return (rad / s) the velocity of the shaft
     */
    @Log
    double getVelocity();

    /**
     * Gets the position of the shaft relative to an arbitrary zero point in radians.
     *
     * @return (rad) the position of the shaft
     */
    @Log
    double getPosition();

    /**
     * Gets the current flowing through the motor in amperes.
     *
     * @return (A) the current flowing through the motor
     */
    @Log
    double getCurrent();

    /**
     * Sets the position of the shaft to the specified value in radians.
     *
     * @param value (rad) the new position of the shaft
     */
    void setPosition(double value);

    /**
     * Sets the velocity of the shaft to the specified value in radians per second.
     *
     * @param value (rad/s) the new velocity of the shaft
     */
    void setVelocity(double value);
}
