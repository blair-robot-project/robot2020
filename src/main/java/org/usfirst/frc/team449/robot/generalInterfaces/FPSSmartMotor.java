package org.usfirst.frc.team449.robot.generalInterfaces;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;


/**
 * A motor with built in advanced capability featuring encoder, current limiting, and gear shifting support.
 * Also features built in FPS conversions.
 */
public interface FPSSmartMotor extends SimpleMotor, Shiftable, Loggable {

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param nativeUnits A distance native units as measured by the encoder.
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    double encoderToFeet(double nativeUnits);

    /**
     * Convert a distance from feet to encoder reading in native units. Note this DOES account for post-encoder
     * gearing.
     *
     * @param feet A distance in feet.
     * @return That distance in native units as measured by the encoder, or null if no encoder CPR was given.
     */
    double feetToEncoder(double feet);

    /**
     * Converts the velocity read by the controllers's getVelocity() method to the FPS of the output shaft. Note this DOES
     * account for post-encoder gearing.
     *
     * @param encoderReading The velocity read from the encoder with no conversions.
     * @return The velocity of the output shaft, in FPS, when the encoder has that reading, or null if no encoder CPR
     * was given.
     */
    double encoderToFPS(double encoderReading);

    /**
     * Converts from the velocity of the output shaft to what the controllers's getVelocity() method would read at that
     * velocity. Note this DOES account for post-encoder gearing.
     *
     * @param FPS The velocity of the output shaft, in FPS.
     * @return What the raw encoder reading would be at that velocity, or null if no encoder CPR was given.
     */
    double FPSToEncoder(double FPS);

    /**
     * Convert from native velocity units to output rotations per second. Note this DOES NOT account for
     * post-encoder gearing.
     *
     * @param nat A velocity in native units.
     * @return That velocity in RPS, or null if no encoder CPR was given.
     */
    Double nativeToRPS(double nat);

    /**
     * Convert from output RPS to the native velocity. Note this DOES NOT account for post-encoder
     * gearing.
     *
     * @param RPS The RPS velocity you want to convert.
     * @return That velocity in native units, or null if no encoder CPR was given.
     */
    double RPSToNative(double RPS);

    /**
     * @return Raw position units for debugging purposes
     */
    double encoderPosition();

    /**
     * Set a position setpoint for the controller.
     */
    public void setPositionSetpoint(double feet);

    /**
     * @return Raw velocity units for debugging purposes
     */
    double encoderVelocity();

    /**
     * Get the velocity of the controller in FPS.
     *
     * @return The controller's velocity in FPS, or null if no encoder CPR was given.
     */
    @Log
    Double getVelocity();

    /**
     * Set the velocity for the motor to go at.
     *
     * @param velocity the desired velocity, on [-1, 1].
     */
    void setVelocity(double velocity);

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    void setVelocityFPS(double velocity);
}
