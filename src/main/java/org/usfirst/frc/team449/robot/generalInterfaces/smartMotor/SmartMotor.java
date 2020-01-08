package org.usfirst.frc.team449.robot.generalInterfaces.smartMotor;

import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

/**
 * A motor with motion profile control and shifting.
 */
public interface SmartMotor extends SimpleMotor, Shiftable, Loggable {
    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    void setPercentVoltage(double percentVoltage);

    /**
     * Set a position setpoint for the Talon.
     *
     * @param feet An absolute position setpoint, in feet.
     */
    void setPositionSetpoint(double feet);

    /**
     * Get the velocity of the CANTalon in FPS.
     *
     * @return The CANTalon's velocity in FPS, or null if no encoder CPR was given.
     */
    @Nullable Double getVelocity();

    /**
     * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in velocity mode.
     *
     * @return The closed-loop error in FPS, or null if no encoder CPR was given.
     */
    @Nullable Double getError();

    /**
     * Get the current velocity setpoint of the Talon in FPS. WARNING: will give garbage if not in velocity mode.
     *
     * @return The closed-loop velocity setpoint in FPS, or null if no encoder CPR was given.
     */
    @Contract(pure = true)
    @Nullable Double getSetpoint();

    /**
     * Get the voltage the Talon is currently drawing from the PDP.
     *
     * @return Voltage in volts.
     */
    @Contract(pure = true)
    double getOutputVoltage();

    /**
     * Get the voltage available for the Talon.
     *
     * @return Voltage in volts.
     */
    @Contract(pure = true)
    double getBatteryVoltage();

    /**
     * Get the current the Talon is currently drawing from the PDP.
     *
     * @return Current in amps.
     */
    @Contract(pure = true)
    double getOutputCurrent();

    /**
     * Get the current control mode of the Talon. Please don't use this for anything other than logging.
     *
     * @return Control mode as a string.
     */
    @Contract(pure = true)
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
    void setGearScaledVelocity(double velocity, gear gear);

    /**
     * @return the position of the motorController in feet, or null of inches per rotation wasn't given.
     */
    @Nullable
    @Contract(pure = true)
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
    @Contract(pure = true)
    boolean getFwdLimitSwitch();

    /**
     * Get the status of the reverse limit switch.
     *
     * @return True if the reverse limit switch is closed, false if it's open or doesn't exist.
     */
    @Contract(pure = true)
    boolean getRevLimitSwitch();

//    /**
//     * Whether this motorController is ready to start running a profile.
//     *
//     * @return True if minNumPointsInBottomBuffer points have been loaded or the top buffer is empty, false otherwise.
//     */
//    boolean readyForMP();
//
//    /**
//     * Whether this motorController has finished running a profile.
//     *
//     * @return True if the active point in the motorController is the last point, false otherwise.
//     */
//    boolean MPIsFinished();
//
//    /**
//     * Starts running the loaded motion profile.
//     */
//    void startRunningMP();
//
//    /**
//     * Holds the current position point in MP mode.
//     */
//    void holdPositionMP();
//
//    /**
//     * Command the motor to achieve a given position, velocity, and acceleration.
//     *
//     * @param pos The desired position in feet.
//     * @param vel The desired velocity in feet/second.
//     * @param acc The desired velocity in feet/second^2.
//     */
//    void executeMPPoint(double pos, double vel, double acc);
//
//    /**
//     * Disables the motorController and loads the given profile into the motorController.
//     *
//     * @param data The profile to load.
//     */
//    void loadProfile(MotionProfileData data);
}
