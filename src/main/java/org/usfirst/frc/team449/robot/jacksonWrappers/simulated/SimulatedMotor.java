package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public interface SimulatedMotor extends Loggable {
    /**
     * Updates the motor's state.
     *
     * @param deltaSecs  (s) - the amount of time that has passed since the motor was last updated
     * @param voltage    (V) - the current voltage applied to the motor
     * @param loadTorque T_l (N*m) - the current torque of load on the motor
     */
    void updatePhysics(double deltaSecs, double voltage, double loadTorque);

    default void updatePhysics(final double deltaSecs, final double voltage) {
        this.updatePhysics(deltaSecs, voltage, 0);
    }

    @Log
    double getVelocity();

    @Log
    double getPosition();

    @Log
    double getCurrent();

    void setPosition(double value);

    void setVelocity(double value);
}
