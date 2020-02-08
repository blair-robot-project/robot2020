package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.jacksonWrappers.*;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.other.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.usfirst.frc.team449.robot.other.Util.clamp;
import static org.usfirst.frc.team449.robot.other.Util.getLogPrefix;

/**
 * Class that implements {@link FPSSmartMotor} without relying on the existence of actual hardware.
 * <p>
 * This class is automatically instantiated by the FPSSmartMotor factory method when the robot is running in a
 * simulation and should not be otherwise referenced in code.
 * </p>
 * <p>
 * The current implementation relies on fictional physics and does not involve
 * </p>
 */
public class FPSSmartMotorSimulated implements FPSSmartMotor {
    /**
     * Nominal bus voltage; used to calculate maximum speed.
     */
    private static final double NOM_BUS_VOLTAGE = 12;
    /**
     * Maximum acceleration in RPS.
     */
    private static final double MAX_ACCEL = 100;
    private static final double MAX_SPEED_COEFF = 10. / 12;
    private static final double MAX_SPEED = NOM_BUS_VOLTAGE * MAX_SPEED_COEFF;
    /**
     * Maximum value for {@link FPSSmartMotorSimulated#integral} (for PID anti-windup).
     */
    private static final double MAX_INTEGRAL = 10;
    /**
     * Used to calculate output voltaeg.
     */
    private static final double MOTOR_RESISTANCE = 1;

    @NotNull
    private final String name;

    @NotNull
    private ControlMode controlMode = ControlMode.Disabled;
    @NotNull
    private PerGearSettings currentGearSettings;
    @NotNull
    private Map<Integer, PerGearSettings> perGearSettings;

    // Log the getters instead because logging the fields doesn't cause physics updates.
    private double percentOutput;
    private double velocity;
    private double position;
    private double busVoltage = NOM_BUS_VOLTAGE;

    @Log
    private double setpoint;

    @Log
    private double lastStateUpdateTime = Clock.currentTimeMillis();
    @Log
    private double targetPercentOutput;
    @Log
    private double error, integral;

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

        //Most of the constructor is stolen from FPSSparkMax.

        this.perGearSettings = new HashMap<>();

        //If given no gear settings, use the default values.
        if (perGearSettings == null || perGearSettings.size() == 0) {
            this.perGearSettings.put(0, new PerGearSettings());
        }
        //Otherwise, map the settings to the gear they are.
        else {
            for (PerGearSettings settings : perGearSettings) {
                this.perGearSettings.put(settings.gear, settings);
            }
        }

        int currentGear;
        //If the starting gear isn't given, assume we start in low gear.
        if (startingGear == null) {
            if (startingGearNum == null) {
                currentGear = Integer.MAX_VALUE;
                for (Integer gear : this.perGearSettings.keySet()) {
                    if (gear < currentGear) {
                        currentGear = gear;
                    }
                }
            } else {
                currentGear = startingGearNum;
            }
        } else {
            currentGear = startingGear.getNumVal();
        }
        currentGearSettings = this.perGearSettings.get(currentGear);
        //Set up gear-based settings.
        setGear(currentGear);
    }

    public void setControlModeAndSetpoint(ControlMode controlMode, double setpoint) {
        // TODO: Possible race condition?
        this.controlMode = controlMode;
        this.setpoint = setpoint;
        this.integral = 0;
    }

    /**
     * Performs simulated PID logic and simulates physical state changes since last call.
     */
    private void updateSimulatedState() {
        // ***** Simulation timing
        double now = Clock.currentTimeMillis();

        double deltaMillis = (now - lastStateUpdateTime);
        if (deltaMillis < 10) return;
        double deltaSecs = deltaMillis * 0.001;

        double newError = 0;
        switch (this.controlMode) {
            case PercentOutput:
                this.targetPercentOutput = setpoint;
                break;

            case Velocity:
            case Position:
                newError = this.setpoint - (this.controlMode == ControlMode.Velocity ? this.velocity : this.position);
                this.integral += (this.error + newError) * 0.5 * deltaSecs;
                this.integral = clamp(this.integral, MAX_INTEGRAL);
                double derivative = (newError - this.error) / deltaSecs;
                if (this.controlMode == ControlMode.Velocity) {
                    this.targetPercentOutput = this.currentGearSettings.kP * newError + this.currentGearSettings.kI * integral + this.currentGearSettings.kD * derivative;
                } else {
                    this.targetPercentOutput = this.currentGearSettings.posKP * newError + this.currentGearSettings.posKI * integral + this.currentGearSettings.posKD * derivative;
                }
                this.error = newError;
                break;

            case Disabled:
                this.lastStateUpdateTime = now;
                return;

            default:
                System.out.println(getLogPrefix(this) + "UNSUPPORTED CONTROL MODE " + this.controlMode);
                return;
        }

        // ***** Physics
        // Use a local to prevent racing observers from seeing invalid value.

        this.percentOutput = this.percentOutput +
                Math.min(Objects.requireNonNullElse(this.currentGearSettings.rampRate, this.busVoltage) * deltaSecs, this.targetPercentOutput - this.percentOutput);
        this.percentOutput = Util.clamp(this.percentOutput);

        this.velocity = this.velocity + Math.min(MAX_ACCEL, MAX_SPEED_COEFF * this.busVoltage * this.percentOutput - this.velocity) * deltaSecs;
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
        this.setControlModeAndSetpoint(ControlMode.PercentOutput, percentVoltage);
    }

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param nativeUnits A distance native units as measured by the encoder.
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    @Override
    public double encoderToFeet(double nativeUnits) {
        return nativeUnits;
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
        this.setControlModeAndSetpoint(ControlMode.Position, this.feetToEncoder(feet));
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
        this.setVelocityFPS(velocity * MAX_SPEED);
    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    @Override
    public void setVelocityFPS(double velocity) {
        this.setControlModeAndSetpoint(ControlMode.Velocity, this.FPSToEncoder(velocity));
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
        return this.getOutputVoltage() / MOTOR_RESISTANCE;
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
        if (currentGearSettings.maxSpeed != null) {
            setVelocityFPS(currentGearSettings.maxSpeed * velocity);
        } else {
            setPercentVoltage(velocity);
        }
    }

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The gear to use the max speed from to scale the velocity.
     */
    @Override
    public void setGearScaledVelocity(double velocity, Shiftable.gear gear) {
        setGearScaledVelocity(velocity, gear.getNumVal());
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
        //Set the current gear
        currentGearSettings = perGearSettings.get(gear);
    }


    @Override
    public String configureLogName() {
        return this.name;
    }
}
