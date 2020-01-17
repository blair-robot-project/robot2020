package org.usfirst.frc.team449.robot.generalInterfaces;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;


/**
 * A motor with built in advanced capability featuring encoder, current limiting, and gear shifting support.
 * Also features built in FPS conversions.
 */
public interface FPSSmartMotor extends SimpleMotor, Shiftable, Loggable {

    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    void setPercentVoltage(double percentVoltage);

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
    void setPositionSetpoint(double feet);

    /**
     * @return Raw velocity units for debugging purposes
     */
    double encoderVelocity();

    /**
     * Get the velocity of the controller in FPS.
     *
     * @return The controller's velocity in FPS, or null if no encoder CPR was given.
     */
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


    /**
     * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in velocity mode.
     *
     * @return The closed-loop error in FPS, or null if no encoder CPR was given.
     */
    double getError();

    /**
     * Get the current velocity setpoint of the Talon in FPS, the position setpoint in feet
     *
     * @return The setpoint in sensible units for the current control mode.
     */
    @Nullable
    Double getSetpoint();

    /**
     * Get the voltage the Talon is currently drawing from the PDP.
     *
     * @return Voltage in volts.
     */
    double getOutputVoltage();

    /**
     * Get the voltage available for the Talon.
     *
     * @return Voltage in volts.
     */
    double getBatteryVoltage();

    /**
     * Get the current the Talon is currently drawing from the PDP.
     *
     * @return Current in amps.
     */
    double getOutputCurrent();

    /**
     * Get the current control mode of the Talon. Please don't use this for anything other than logging.
     *
     * @return Control mode as a string.
     */
    String getControlMode();

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The number of the gear to use the max speed from to scale the velocity.
     */
    void setGearScaledVelocity(double velocity, int gear);

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The gear to use the max speed from to scale the velocity.
     */
    void setGearScaledVelocity(double velocity, Shiftable.gear gear);

    /**
     * @return Feedforward calculator for this gear
     */
    SimpleMotorFeedforward getCurrentGearFeedForward();
    /**
     * @return the position of the talon in feet, or null of inches per rotation wasn't given.
     */
    Double getPositionFeet();

    /**
     * Resets the position of the Talon to 0.
     */
    void resetPosition();

    /**
     * Get the status of the forwards limit switch.
     *
     * @return True if the forwards limit switch is closed, false if it's open or doesn't exist.
     */
    boolean getFwdLimitSwitch();
    /**
     * Get the status of the reverse limit switch.
     *
     * @return True if the reverse limit switch is closed, false if it's open or doesn't exist.
     */
    boolean getRevLimitSwitch();

    boolean isInhibitedForward();

    boolean isInhibitedReverse();

    @Override
    default LayoutType configureLayoutType() {
        return BuiltInLayouts.kGrid;
    }
}
