package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveSparkMax;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveVictor;
import org.usfirst.frc.team449.robot.other.Clock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that implements {@link FPSSmartMotor} without relying on hardware.
 * This class is automatically instantiated by the FPSSmartMotor factory method when the robot is running in a
 * simulation and should not be otherwise referenced in code.
 */
public class FPSSmartMotorSimulated implements FPSSmartMotor {
    private static final double MAX_ACCEL = 100;
    private final String name;

    private double percentOutput;
    private double velocity;
    private double position;
    private double busVoltage = 10;

    @Log
    private double setpoint;

    private ControlMode controlMode = ControlMode.Disabled;

    @Log
    private double lastStateUpdateTime = Clock.currentTimeMillis();

    public FPSSmartMotorSimulated(@JsonProperty(required = true) Type type,
                                  @JsonProperty(required = true) int port,
                                  @JsonProperty(required = true) boolean enableBrakeMode,
                                  @Nullable String name,
                                  boolean reverseOutput,
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
                                  // Spark-specific
                                  @Nullable HashMap<CANSparkMaxLowLevel.PeriodicFrame, Integer> sparkStatusFramesMap,
                                  @Nullable final Integer controlFrameRateMillis,
                                  // Talon-specific
                                  @Nullable HashMap<StatusFrameEnhanced, Integer> talonStatusFramesMap,
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

        this.name = "FAKE " + (name != null ? name : port);
    }

    /**
     * Performs simulated PID logic and simulates physical state changes since last call.
     */
    private void updateSimulatedState() {
        double now = Clock.currentTimeMillis();
        double deltaSecs = (now - lastStateUpdateTime) / 1000.0;

        switch (this.controlMode) {
            case PercentOutput:
                this.percentOutput = this.setpoint;
                break;
            case Velocity:
                this.percentOutput = Math.min(this.busVoltage, 1.1 * (this.setpoint - this.velocity));
                break;
            case Position:
                this.percentOutput = Math.min(this.busVoltage, 1.1 * (this.setpoint - this.position));
                break;
        }

        this.velocity += Math.min(MAX_ACCEL, 10 * (this.busVoltage * this.percentOutput - this.velocity)) * deltaSecs;
        this.position += this.velocity * deltaSecs;

        this.lastStateUpdateTime = now;
    }

    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    @Override
    public void setPercentVoltage(double percentVoltage) {
        System.out.println("SIM MOTOR %Voltage: " + percentVoltage);
        this.setpoint = percentVoltage;
        this.controlMode = ControlMode.PercentOutput;
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
        return feet;
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
        return encoderReading;
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
        return FPS;
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
        return nat;
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
        return RPS;
    }

    /**
     * @return Raw position units for debugging purposes
     */
    @Override
    @Log
    public double encoderPosition() {
        this.updateSimulatedState();
        return this.position;
    }

    /**
     * Set a position setpoint for the controller.
     *
     * @param feet
     */
    @Override
    public void setPositionSetpoint(double feet) {
        this.setpoint = this.feetToEncoder(feet);
        this.controlMode = ControlMode.Position;
    }

    /**
     * @return Raw velocity units for debugging purposes
     */
    @Override
    @Log
    public double encoderVelocity() {
        this.updateSimulatedState();
        return this.velocity;
    }

    /**
     * Get the velocity of the controller in FPS.
     *
     * @return The controller's velocity in FPS, or null if no encoder CPR was given.
     */
    @Override
    public Double getVelocity() {
        return this.encoderToFPS(this.encoderVelocity());
    }

    /**
     * Set the velocity for the motor to go at.
     *
     * @param velocity the desired velocity, on [-1, 1].
     */
    @Override
    public void setVelocity(double velocity) {
        this.setpoint = velocity;
        this.controlMode = ControlMode.Velocity;
    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    @Override
    public void setVelocityFPS(double velocity) {
        this.setpoint = this.FPSToEncoder(velocity);
        this.controlMode = ControlMode.Velocity;
    }

    /**
     * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in velocity mode.
     *
     * @return The closed-loop error in FPS, or null if no encoder CPR was given.
     */
    @Override
    public double getError() {
        return this.encoderToFPS(this.setpoint - this.velocity);
    }

    /**
     * Get the current velocity setpoint of the Talon in FPS, the position setpoint in feet
     *
     * @return The setpoint in sensible units for the current control mode.
     */
    @Override
    public @Nullable Double getSetpoint() {
        switch (this.controlMode) {
            case Velocity:
                return this.encoderToFPS(this.setpoint);
            case Position:
                return this.encoderToFeet(this.setpoint);
            default:
                return null;
        }
    }

    /**
     * Get the voltage the Talon is currently drawing from the PDP.
     *
     * @return Voltage in volts.
     */
    @Override
    @Log
    public double getOutputVoltage() {
        this.updateSimulatedState();
        return this.getBatteryVoltage() * this.percentOutput;
    }

    /**
     * Get the voltage available for the Talon.
     *
     * @return Voltage in volts.
     */
    @Override
    @Log
    public double getBatteryVoltage() {
        return this.busVoltage;
    }

    /**
     * Get the current the Talon is currently drawing from the PDP.
     *
     * @return Current in amps.
     */
    @Override
    @Log
    public double getOutputCurrent() {
        return 0;
    }

    /**
     * Get the current control mode of the Talon. Please don't use this for anything other than logging.
     *
     * @return Control mode as a string.
     */
    @Override
    @Log
    public String getControlMode() {
        return this.controlMode.name();
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
        return this.encoderToFeet(this.encoderPosition());
    }

    /**
     * Resets the position of the Talon to 0.
     */
    @Override
    public void resetPosition() {
        this.position = 0;
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

    @Override
    public String configureLogName() {
        return this.name;
    }
}
