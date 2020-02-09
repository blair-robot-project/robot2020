package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveSparkMax;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveVictor;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.other.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoubleSupplier;

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
     * (V) Nominal bus voltage; used to calculate maximum speed.
     */
    private static final double NOM_BUS_VOLTAGE = 12;
    /**
     * (Kg*m) Moment of moving parts.
     */
    private static final double MOMENT = 1;
    private static final double MAX_SPEED_COEFF = 10. / 12;
    private static final double MAX_SPEED = NOM_BUS_VOLTAGE * MAX_SPEED_COEFF;
    /**
     * (V) Used to calculate output current.
     */
    private static final double MOTOR_RESISTANCE = 1;
    /**
     * (N*m / V) Torque per volt due to force of motor.
     */
    private static final double TORQUE_COEFF = 450;
    /**
     * (N*m / (R/s)) Torque per RPS due to motor internal friction.
     */
    private static final double MOTOR_FRICTION_COEFF = -1;
    /**
     * Maximum PID integral for anti-windup.
     */
    private static final double MAX_INTEGRAL = 100;

    @NotNull
    private final String name;
    private final Type type;
    private final int port;
    private final boolean reverseOutput;
    private final double feetPerRotation;

    /**
     * (Depends on mode)
     */
    @Log
    private double setpoint;
    @NotNull
    private ControlMode controlMode = ControlMode.Disabled;
    @NotNull
    private final FPSSmartMotorSimulated.PID pid = new PID(MAX_INTEGRAL, () -> this.setpoint, 0, 0, 0);
    @NotNull
    private PerGearSettings currentGearSettings;
    @NotNull
    private final Map<Integer, PerGearSettings> perGearSettings;

    // Log the getters instead because logging the fields doesn't cause physics updates.
    private double percentOutput;
    /**
     * (R/s) Signed rotations per second
     */
    private double velocity;
    /**
     * Absolute rotation value
     */
    private double position;
    /**
     * (V)
     */
    private final double busVoltage = NOM_BUS_VOLTAGE;

    @Log
    private double lastStateUpdateTime = Clock.currentTimeMillis();

    public FPSSmartMotorSimulated(@JsonProperty(required = true) final Type type,
                                  @JsonProperty(required = true) final int port,
                                  @JsonProperty(required = true) final boolean enableBrakeMode,
                                  @Nullable final String name,
                                  final boolean reverseOutput,
                                  @Nullable final PDP PDP,
                                  @Nullable final Boolean fwdLimitSwitchNormallyOpen,
                                  @Nullable final Boolean revLimitSwitchNormallyOpen,
                                  @Nullable final Integer remoteLimitSwitchID,
                                  @Nullable final Double fwdSoftLimit,
                                  @Nullable final Double revSoftLimit,
                                  @Nullable final Double postEncoderGearing,
                                  @Nullable final Double feetPerRotation,
                                  @Nullable final Integer currentLimit,
                                  final boolean enableVoltageComp,
                                  @Nullable final List<PerGearSettings> perGearSettings,
                                  @Nullable final Shiftable.gear startingGear,
                                  @Nullable final Integer startingGearNum,
                                  // Spark-specific
                                  @Nullable final HashMap<CANSparkMaxLowLevel.PeriodicFrame, Integer> sparkStatusFramesMap,
                                  @Nullable final Integer controlFrameRateMillis,
                                  // Talon-specific
                                  @Nullable final HashMap<StatusFrameEnhanced, Integer> talonStatusFramesMap,
                                  @Nullable final Map<ControlFrame, Integer> controlFrameRatesMillis,
                                  @Nullable final RunningLinRegComponent voltagePerCurrentLinReg,
                                  @Nullable final Integer voltageCompSamples,
                                  @Nullable final FeedbackDevice feedbackDevice,
                                  @Nullable final Integer encoderCPR,
                                  @Nullable final Boolean reverseSensor,
                                  @Nullable final Double updaterProcessPeriodSecs,
                                  @Nullable final List<SlaveTalon> slaveTalons,
                                  @Nullable final List<SlaveVictor> slaveVictors,
                                  @Nullable final List<SlaveSparkMax> slaveSparks) {
        this.type = type;
        this.port = port;
        this.reverseOutput = reverseOutput;
        this.feetPerRotation = Objects.requireNonNullElse(feetPerRotation, 1.0);
        this.name = String.format("FAKE %s %d %s", type, port, name);

        //Most of the constructor is stolen from FPSSparkMax.

        this.perGearSettings = new HashMap<>();

        //If given no gear settings, use the default values.
        if (perGearSettings == null || perGearSettings.size() == 0) {
            this.perGearSettings.put(0, new PerGearSettings());
        }
        //Otherwise, map the settings to the gear they are.
        else {
            for (final PerGearSettings settings : perGearSettings) {
                this.perGearSettings.put(settings.gear, settings);
            }
        }

        int currentGear;
        //If the starting gear isn't given, assume we start in low gear.
        if (startingGear == null) {
            if (startingGearNum == null) {
                currentGear = Integer.MAX_VALUE;
                for (final Integer gear : this.perGearSettings.keySet()) {
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
        this.currentGearSettings = this.perGearSettings.get(currentGear);
        //Set up gear-based settings.
        this.setGear(currentGear);
    }

    public void setControlModeAndSetpoint(final ControlMode controlMode, final double setpoint) {
        this.controlMode = controlMode;
        this.setpoint = setpoint;

        switch (controlMode) {
            case Velocity:
                this.pid.reconfigure(this.currentGearSettings.kP, this.currentGearSettings.kI, this.currentGearSettings.kD);
                break;
            case Position:
                this.pid.reconfigure(this.currentGearSettings.posKP, this.currentGearSettings.posKI, this.currentGearSettings.posKD);
                break;

            case Current:
            case Follower:
                System.out.println("WARNING: Not yet implemented.");
                break;

            case MotionProfile:
            case MotionMagic:
            case MotionProfileArc:
                System.out.println("WARNING: Unlikely to be implemented.");
                break;

            case Disabled:
            case PercentOutput:
                break;
        }
    }

    /**
     * Performs simulated PID logic and simulates physical state changes since last call.
     */
    private void updateSimulatedState() {
        // ***** Simulation timing
        final double now = Clock.currentTimeMillis();

        final double deltaMillis = (now - this.lastStateUpdateTime);
        if (deltaMillis < 10) return;
        final double deltaSecs = deltaMillis * 0.001;

        final double targetPercentOutput;
        switch (this.controlMode) {
            case PercentOutput:
                targetPercentOutput = this.setpoint;
                break;

            case Velocity:
            case Position:
                final double newActualValue = (this.controlMode == ControlMode.Velocity ? this.velocity : this.position);
                this.pid.update(newActualValue, deltaSecs);
                targetPercentOutput = this.pid.getOutput();
                break;

            case Disabled:
                this.lastStateUpdateTime = now;
                return;

            default:
                System.out.println(getLogPrefix(this) + "UNSUPPORTED CONTROL MODE " + this.controlMode);
                return;
        }

        // ***** Physics
        this.percentOutput = this.percentOutput +
                Math.min(Objects.requireNonNullElse(this.currentGearSettings.rampRate, this.busVoltage) * deltaSecs, targetPercentOutput - this.percentOutput);
        this.percentOutput = Util.clamp(this.percentOutput);
        if (this.reverseOutput) this.percentOutput *= -1;

        final double motorTorque = TORQUE_COEFF * this.busVoltage * this.percentOutput;
        final double frictionTorque = MOTOR_FRICTION_COEFF * this.velocity;
        final double netTorque = motorTorque + frictionTorque;
        final double angularAcceleration = netTorque / MOMENT;

        this.velocity += angularAcceleration * deltaSecs;
        this.position += this.velocity * deltaSecs;

        this.lastStateUpdateTime = now;
    }

    private static class PID implements Loggable {
        @Log
        private double error, integral, derivative;
        private final DoubleSupplier getSetPoint;
        private double kP;
        private double kI;
        private double kD;
        private final double maxIntegral;

        public PID(final double maxIntegral, final DoubleSupplier getSetPoint, final double kP, final double kI, final double kD) {
            this.maxIntegral = maxIntegral;
            this.getSetPoint = getSetPoint;
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
        }

        public void update(final double newActualValue, final double deltaSecs) {
            final double newError = this.getSetPoint.getAsDouble() - newActualValue;
            this.integral += (this.error + newError) * 0.5 * deltaSecs;
            this.integral = clamp(this.integral, this.maxIntegral);
            this.derivative = (newError - this.error) / deltaSecs;
            this.error = newError;
        }

        @Log
        public double getOutput() {
            return this.kP * this.error + this.kI * this.integral + this.kD * this.derivative;
        }

        public void reconfigure(final double kP, final double kI, final double kD) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.error = this.derivative = this.integral = 0;
        }
    }

    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    @Override
    public void setPercentVoltage(final double percentVoltage) {
        this.setControlModeAndSetpoint(ControlMode.PercentOutput, percentVoltage);
    }

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param nativeUnits A distance native units as measured by the encoder.
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    @Override
    public double encoderToFeet(final double nativeUnits) {
        return nativeUnits * this.feetPerRotation;
    }

    /**
     * Convert a distance from feet to encoder reading in native units. Note this DOES account for post-encoder
     * gearing.
     *
     * @param feet A distance in feet.
     * @return That distance in native units as measured by the encoder, or null if no encoder CPR was given.
     */
    @Override
    public double feetToEncoder(final double feet) {
        return feet / this.feetPerRotation;
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
    public double encoderToFPS(final double encoderReading) {
        return encoderReading * this.feetPerRotation;
    }

    /**
     * Converts from the velocity of the output shaft to what the controllers's getVelocity() method would read at that
     * velocity. Note this DOES account for post-encoder gearing.
     *
     * @param FPS The velocity of the output shaft, in FPS.
     * @return What the raw encoder reading would be at that velocity, or null if no encoder CPR was given.
     */
    @Override
    public double FPSToEncoder(final double FPS) {
        return FPS / this.feetPerRotation;
    }

    /**
     * Convert from native velocity units to output rotations per second. Note this DOES NOT account for
     * post-encoder gearing.
     *
     * @param nat A velocity in native units.
     * @return That velocity in RPS, or null if no encoder CPR was given.
     */
    @Override
    public Double nativeToRPS(final double nat) {
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
    public double RPSToNative(final double RPS) {
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
     */
    @Override
    public void setPositionSetpoint(final double feet) {
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
    public void setVelocity(final double velocity) {
        this.setVelocityFPS(velocity * MAX_SPEED);
    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    @Override
    public void setVelocityFPS(final double velocity) {
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
    @Nullable
    public Double getSetpoint() {
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
    public void setGearScaledVelocity(final double velocity, final int gear) {
        if (this.currentGearSettings.maxSpeed != null) {
            this.setVelocityFPS(this.currentGearSettings.maxSpeed * velocity);
        } else {
            this.setPercentVoltage(velocity);
        }
    }

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The gear to use the max speed from to scale the velocity.
     */
    @Override
    public void setGearScaledVelocity(final double velocity, final Shiftable.gear gear) {
        this.setGearScaledVelocity(velocity, gear.getNumVal());
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

    @Override
    public int getPort() {
        return this.port;
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
    public void setGear(final int gear) {
        //Set the current gear
        this.currentGearSettings = this.perGearSettings.get(gear);
    }


    @Override
    public String configureLogName() {
        return this.name;
    }
}
