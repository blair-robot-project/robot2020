package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveSparkMax;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveVictor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to be . This class is automatically instantiated should not
 */
public class FPSSmartMotorSimulated implements FPSSmartMotor {
    public FPSSmartMotorSimulated(@JsonProperty(required = true) Type type,
                                  @JsonProperty(required = true) int port,
                                  @Nullable String name,
                                  boolean reverseOutput,
                                  @JsonProperty(required = true) boolean enableBrakeMode,
                                  @Nullable PDP PDP,
                                  @Nullable Boolean fwdLimitSwitchNormallyOpen,
                                  @Nullable Boolean revLimitSwitchNormallyOpen,
                                  @Nullable Integer remoteLimitSwitchID,
                                  @Nullable Double fwdSoftLimit,
                                  @Nullable Double revSoftLimit,
                                  @Nullable Double postEncoderGearing,
                                  @Nullable Double feetPerRotation,
                                  @Nullable Integer currentLimit,
                                  boolean enableVoltageComp,
                                  @Nullable List<PerGearSettings> perGearSettings,
                                  @Nullable Shiftable.gear startingGear,
                                  @Nullable Integer startingGearNum,
                                  @Nullable HashMap<StatusFrameEnhanced, Integer> sparkStatusFramesMap,
                                  @Nullable HashMap<CANSparkMaxLowLevel.PeriodicFrame, Integer> talonStatusFramesMap,
                                  // Spark-specific
                                  @Nullable final Integer controlFrameRateMillis,
                                  // Talon-specific
                                  @Nullable final Map<ControlFrame, Integer> controlFrameRatesMillis,
                                  @Nullable RunningLinRegComponent voltagePerCurrentLinReg,
                                  @Nullable Integer voltageCompSamples,
                                  @Nullable FeedbackDevice feedbackDevice,
                                  @Nullable Integer encoderCPR,
                                  @Nullable Boolean reverseSensor,
                                  @Nullable Double updaterProcessPeriodSecs,
                                  @Nullable List<SlaveTalon> slaveTalons,
                                  @Nullable List<SlaveVictor> slaveVictors,
                                  @Nullable List<SlaveSparkMax> slaveSparks) {
    }

    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    @Override
    public void setPercentVoltage(double percentVoltage) {
    }

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param nativeUnits A distance native units as measured by the encoder.
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    @Override
    public double encoderToFeet(double nativeUnits) {
        return 0;
    }

    /**
     * Convert a distance from feet to encoder reading in native units. Note this DOES account for post-encoder
     * gearing.
     *
     * @param feet A distance in feet.
     * @return That distance in native units as measured by the encoder, or null if no encoder CPR was given.
     */
    @Override
    public double feetToEncoder(double feet) {
        return 0;
    }

    /**
     * Converts the velocity read by the controllers's getVelocity() method to the FPS of the output shaft. Note this DOES
     * account for post-encoder gearing.
     *
     * @param encoderReading The velocity read from the encoder with no conversions.
     * @return The velocity of the output shaft, in FPS, when the encoder has that reading, or null if no encoder CPR
     * was given.
     */
    @Override
    public double encoderToFPS(double encoderReading) {
        return 0;
    }

    /**
     * Converts from the velocity of the output shaft to what the controllers's getVelocity() method would read at that
     * velocity. Note this DOES account for post-encoder gearing.
     *
     * @param FPS The velocity of the output shaft, in FPS.
     * @return What the raw encoder reading would be at that velocity, or null if no encoder CPR was given.
     */
    @Override
    public double FPSToEncoder(double FPS) {
        return 0;
    }

    /**
     * Convert from native velocity units to output rotations per second. Note this DOES NOT account for
     * post-encoder gearing.
     *
     * @param nat A velocity in native units.
     * @return That velocity in RPS, or null if no encoder CPR was given.
     */
    @Override
    public Double nativeToRPS(double nat) {
        return 0.0;
    }

    /**
     * Convert from output RPS to the native velocity. Note this DOES NOT account for post-encoder
     * gearing.
     *
     * @param RPS The RPS velocity you want to convert.
     * @return That velocity in native units, or null if no encoder CPR was given.
     */
    @Override
    public double RPSToNative(double RPS) {
        return 0;
    }

    /**
     * @return Raw position units for debugging purposes
     */
    @Override
    public double encoderPosition() {
        return 0;
    }

    /**
     * Set a position setpoint for the controller.
     *
     * @param feet
     */
    @Override
    public void setPositionSetpoint(double feet) {

    }

    /**
     * @return Raw velocity units for debugging purposes
     */
    @Override
    public double encoderVelocity() {
        return 0;
    }

    /**
     * Get the velocity of the controller in FPS.
     *
     * @return The controller's velocity in FPS, or null if no encoder CPR was given.
     */
    @Override
    public Double getVelocity() {
        return 0.0;
    }

    /**
     * Set the velocity for the motor to go at.
     *
     * @param velocity the desired velocity, on [-1, 1].
     */
    @Override
    public void setVelocity(double velocity) {

    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    @Override
    public void setVelocityFPS(double velocity) {

    }

    /**
     * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in velocity mode.
     *
     * @return The closed-loop error in FPS, or null if no encoder CPR was given.
     */
    @Override
    public double getError() {
        return 0;
    }

    /**
     * Get the current velocity setpoint of the Talon in FPS, the position setpoint in feet
     *
     * @return The setpoint in sensible units for the current control mode.
     */
    @Override
    public @Nullable Double getSetpoint() {
        return null;
    }

    /**
     * Get the voltage the Talon is currently drawing from the PDP.
     *
     * @return Voltage in volts.
     */
    @Override
    public double getOutputVoltage() {
        return 0;
    }

    /**
     * Get the voltage available for the Talon.
     *
     * @return Voltage in volts.
     */
    @Override
    public double getBatteryVoltage() {
        return 0;
    }

    /**
     * Get the current the Talon is currently drawing from the PDP.
     *
     * @return Current in amps.
     */
    @Override
    public double getOutputCurrent() {
        return 0;
    }

    /**
     * Get the current control mode of the Talon. Please don't use this for anything other than logging.
     *
     * @return Control mode as a string.
     */
    @Override
    public String getControlMode() {
        return "";
    }

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The number of the gear to use the max speed from to scale the velocity.
     */
    @Override
    public void setGearScaledVelocity(double velocity, int gear) {

    }

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The gear to use the max speed from to scale the velocity.
     */
    @Override
    public void setGearScaledVelocity(double velocity, gear gear) {

    }

    /**
     * @return Feedforward calculator for this gear
     */
    @Override
    public SimpleMotorFeedforward getCurrentGearFeedForward() {
        return null;
    }

    /**
     * @return the position of the talon in feet, or null of inches per rotation wasn't given.
     */
    @Override
    public Double getPositionFeet() {
        return 0.0;
    }

    /**
     * Resets the position of the Talon to 0.
     */
    @Override
    public void resetPosition() {

    }

    /**
     * Get the status of the forwards limit switch.
     *
     * @return True if the forwards limit switch is closed, false if it's open or doesn't exist.
     */
    @Override
    public boolean getFwdLimitSwitch() {
        return false;
    }

    /**
     * Get the status of the reverse limit switch.
     *
     * @return True if the reverse limit switch is closed, false if it's open or doesn't exist.
     */
    @Override
    public boolean getRevLimitSwitch() {
        return false;
    }

    @Override
    public boolean isInhibitedForward() {
        return false;
    }

    @Override
    public boolean isInhibitedReverse() {
        return false;
    }

    /**
     * @return The gear this subsystem is currently in.
     */
    @Override
    public int getGear() {
        return 0;
    }

    /**
     * Shift to a specific gear.
     *
     * @param gear Which gear to shift to.
     */
    @Override
    public void setGear(int gear) {
    }
}
