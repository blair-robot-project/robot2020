package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component wrapper on the CTRE {@link TalonSRX}, with unit conversions to/from FPS built in. Every non-unit-conversion
 * in this class takes arguments in post-gearing FPS.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FPSTalon implements FPSSmartMotor {

    Faults faults = new Faults();

    /**
     * The CTRE CAN Talon SRX that this class is a wrapper on
     */
    @NotNull
    protected final TalonSRX canTalon;

    /**
     * The PDP this Talon is connected to.
     */
    @Nullable
    @Log.Exclude
    protected final PDP PDP;
    /**
     * The counts per rotation of the encoder being used, or null if there is no encoder.
     */
    @Nullable
    private final Integer encoderCPR;
    /**
     * The coefficient the output changes by after being measured by the encoder, e.g. this would be 1/70 if there was a
     * 70:1 gearing between the encoder and the final output.
     */
    private final double postEncoderGearing;
    /**
     * The number of feet travelled per rotation of the motor this is attached to, or null if there is no encoder.
     */
    private final double feetPerRotation;
    /**
     * The period for bottomBufferLoader, in seconds.
     */
    private final double updaterProcessPeriodSecs;
    /**
     * A list of all the gears this robot has and their settings.
     */
    @NotNull
    private final Map<Integer, PerGearSettings> perGearSettings;
    /**
     * The talon's name, used for logging purposes.
     */
    @NotNull
    private final String name;
    /**
     * The component for doing linear regression to find the resistance.
     */
    @Nullable
    private final RunningLinRegComponent voltagePerCurrentLinReg;
    /**
     * Whether the forwards or reverse limit switches are normally open or closed, respectively.
     */
    private final boolean fwdLimitSwitchNormallyOpen, revLimitSwitchNormallyOpen;
    /**
     * The settings currently being used by this Talon.
     */
    @NotNull
    protected PerGearSettings currentGearSettings;
    /**
     * The most recently set setpoint.
     */
    private double setpoint;
    /**
     * RPS as used in a unit conversion method. Field to avoid garbage collection.
     */
    private Double RPS;

    /**
     * The setpoint in native units. Field to avoid garbage collection.
     */
    private double nativeSetpoint;

    /**
     * Default constructor.
     *
     * @param port                       CAN port of this Talon.
     * @param name                       The talon's name, used for logging purposes. Defaults to talon_portnum
     * @param reverseOutput              Whether to reverse the output.
     * @param enableBrakeMode            Whether to brake or coast when stopped.
     * @param voltagePerCurrentLinReg    The component for doing linear regression to find the resistance.
     * @param PDP                        The PDP this Talon is connected to.
     * @param fwdLimitSwitchNormallyOpen Whether the forward limit switch is normally open or closed. If this is null,
     *                                   the forward limit switch is disabled.
     * @param revLimitSwitchNormallyOpen Whether the reverse limit switch is normally open or closed. If this is null,
     *                                   the reverse limit switch is disabled.
     * @param remoteLimitSwitchID        The CAN port of the Talon the limit switch to use for this talon is plugged
     *                                   into, or null to not use a limit switch or use the limit switch plugged into
     *                                   this talon.
     * @param fwdSoftLimit               The forward software limit, in feet. If this is null, the forward software
     *                                   limit is disabled. Ignored if there's no encoder.
     * @param revSoftLimit               The reverse software limit, in feet. If this is null, the reverse software
     *                                   limit is disabled. Ignored if there's no encoder.
     * @param postEncoderGearing         The coefficient the output changes by after being measured by the encoder, e.g.
     *                                   this would be 1/70 if there was a 70:1 gearing between the encoder and the
     *                                   final output. Defaults to 1.
     * @param feetPerRotation            The number of feet travelled per rotation of the motor this is attached to.
     *                                   Defaults to 1.
     * @param currentLimit               The max amps this device can draw. If this is null, no current limit is used.
     * @param enableVoltageComp          Whether or not to use voltage compensation. Defaults to false.
     * @param voltageCompSamples         The number of 1-millisecond samples to use for voltage compensation. Defaults
     *                                   to 32.
     * @param feedbackDevice             The type of encoder used to measure the output velocity of this motor. Can be
     *                                   null if there is no encoder attached to this Talon.
     * @param encoderCPR                 The counts per rotation of the encoder on this Talon. Can be null if
     *                                   feedbackDevice is, but otherwise must have a value.
     * @param reverseSensor              Whether or not to reverse the reading from the encoder on this Talon. Ignored
     *                                   if feedbackDevice is null. Defaults to false.
     * @param perGearSettings            The settings for each gear this motor has. Can be null to use default values
     *                                   and gear # of zero. Gear numbers can't be repeated.
     * @param startingGear               The gear to start in. Can be null to use startingGearNum instead.
     * @param startingGearNum            The number of the gear to start in. Ignored if startingGear isn't null.
     *                                   Defaults to the lowest gear.
     * @param updaterProcessPeriodSecs   The period for the {@link Notifier} that moves points between the MP buffers, in
     *                                   seconds. Defaults to 0.005.
     * @param statusFrameRatesMillis     The update rates, in millis, for each of the Talon status frames.
     * @param controlFrameRatesMillis    The update rate, in milliseconds, for each of the control frame.
     * @param slaveTalons                The other {@link TalonSRX}s that are slaved to this one.
     * @param slaveVictors               The {@link com.ctre.phoenix.motorcontrol.can.VictorSPX}s that are slaved to
     *                                   this Talon.
     * @param slaveSparkMaxs             The Spark/Neo combinations slaved to this Talon.
     */
    @JsonCreator
    public FPSTalon(@JsonProperty(required = true) int port,
                    @Nullable String name,
                    boolean reverseOutput,
                    @JsonProperty(required = true) boolean enableBrakeMode,
                    @Nullable RunningLinRegComponent voltagePerCurrentLinReg,
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
                    @Nullable Integer voltageCompSamples,
                    @Nullable FeedbackDevice feedbackDevice,
                    @Nullable Integer encoderCPR,
                    boolean reverseSensor,
                    @Nullable List<PerGearSettings> perGearSettings,
                    @Nullable Shiftable.gear startingGear,
                    @Nullable Integer startingGearNum,
                    @Nullable Double updaterProcessPeriodSecs,
                    @Nullable Map<StatusFrameEnhanced, Integer> statusFrameRatesMillis,
                    @Nullable Map<ControlFrame, Integer> controlFrameRatesMillis,
                    @Nullable List<SlaveTalon> slaveTalons,
                    @Nullable List<SlaveVictor> slaveVictors,
                    @Nullable List<SlaveSparkMax> slaveSparks) {
        //Instantiate the base CANTalon this is a wrapper on.
        canTalon = new TalonSRX(port);
        //Set the name to the given one or to talon_portnum
        this.name = name != null ? name : ("talon_" + port);
        //Set this to false because we only use reverseOutput for slaves.
        canTalon.setInverted(reverseOutput);
        //Set brake mode
        canTalon.setNeutralMode(enableBrakeMode ? NeutralMode.Brake : NeutralMode.Coast);
        //Reset the position
        resetPosition();

        this.PDP = PDP;
        this.voltagePerCurrentLinReg = voltagePerCurrentLinReg;

        //Set frame rates
        if (controlFrameRatesMillis != null) {
            for (ControlFrame controlFrame : controlFrameRatesMillis.keySet()) {
                canTalon.setControlFramePeriod(controlFrame, controlFrameRatesMillis.get(controlFrame));
            }
        }
        if (statusFrameRatesMillis != null) {
            for (StatusFrameEnhanced statusFrame : statusFrameRatesMillis.keySet()) {
                canTalon.setStatusFramePeriod(statusFrame, statusFrameRatesMillis.get(statusFrame), 0);
            }
        }

        //Set fields
        this.feetPerRotation = feetPerRotation != null ? feetPerRotation : 1;
        this.updaterProcessPeriodSecs = updaterProcessPeriodSecs != null ? updaterProcessPeriodSecs : 0.005;

        //Initialize
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

        //Only enable the limit switches if it was specified if they're normally open or closed.
        if (fwdLimitSwitchNormallyOpen != null) {
            if (remoteLimitSwitchID != null) {
                canTalon.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                        fwdLimitSwitchNormallyOpen ? LimitSwitchNormal.NormallyOpen : LimitSwitchNormal.NormallyClosed,
                        remoteLimitSwitchID, 0);
            } else {
                canTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                        fwdLimitSwitchNormallyOpen ? LimitSwitchNormal.NormallyOpen :
                                LimitSwitchNormal.NormallyClosed, 0);
            }
            this.fwdLimitSwitchNormallyOpen = fwdLimitSwitchNormallyOpen;
        } else {
            canTalon.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
            this.fwdLimitSwitchNormallyOpen = true;
        }
        if (revLimitSwitchNormallyOpen != null) {
            if (remoteLimitSwitchID != null) {
                canTalon.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                        revLimitSwitchNormallyOpen ? LimitSwitchNormal.NormallyOpen : LimitSwitchNormal.NormallyClosed,
                        remoteLimitSwitchID, 0);
            } else {
                canTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                        revLimitSwitchNormallyOpen ? LimitSwitchNormal.NormallyOpen :
                                LimitSwitchNormal.NormallyClosed, 0);
            }
            this.revLimitSwitchNormallyOpen = revLimitSwitchNormallyOpen;
        } else {
            canTalon.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
            this.revLimitSwitchNormallyOpen = true;
        }

        //Set up the feedback device if it exists.
        if (feedbackDevice != null) {
            //CTRE encoder use RPM instead of native units, and can be used as QuadEncoders, so we switch them to avoid
            //having to support RPM.
            if (feedbackDevice.equals(FeedbackDevice.CTRE_MagEncoder_Absolute) ||
                    feedbackDevice.equals(FeedbackDevice.CTRE_MagEncoder_Relative)) {
                canTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
            } else {
                canTalon.configSelectedFeedbackSensor(feedbackDevice, 0, 0);
            }
            this.encoderCPR = encoderCPR;
            canTalon.setSensorPhase(reverseSensor);

            //Only enable the software limits if they were given a value and there's an encoder.
            if (fwdSoftLimit != null) {
                canTalon.configForwardSoftLimitEnable(true, 0);
                canTalon.configForwardSoftLimitThreshold((int) feetToEncoder(fwdSoftLimit), 0);
            } else {
                canTalon.configForwardSoftLimitEnable(false, 0);
            }
            if (revSoftLimit != null) {
                canTalon.configReverseSoftLimitEnable(true, 0);
                canTalon.configReverseSoftLimitThreshold((int) feetToEncoder(revSoftLimit), 0);
            } else {
                canTalon.configReverseSoftLimitEnable(false, 0);
            }
        } else {
            this.encoderCPR = null;
            canTalon.configSelectedFeedbackSensor(FeedbackDevice.None, 0, 0);
        }

        //postEncoderGearing defaults to 1
        this.postEncoderGearing = postEncoderGearing != null ? postEncoderGearing : 1.;

        //Set up gear-based settings.
        setGear(currentGear);

        //Set the current limit if it was given
        if (currentLimit != null) {
            canTalon.configContinuousCurrentLimit(currentLimit, 0);
            canTalon.configPeakCurrentDuration(0, 0);
            canTalon.configPeakCurrentLimit(0, 0); // No duration
            canTalon.enableCurrentLimit(true);
        } else {
            //If we don't have a current limit, disable current limiting.
            canTalon.enableCurrentLimit(false);
        }

        //Enable or disable voltage comp
        canTalon.enableVoltageCompensation(enableVoltageComp);
        canTalon.configVoltageCompSaturation(12, 0);
        int notNullVoltageCompSamples = voltageCompSamples != null ? voltageCompSamples : 32;
        canTalon.configVoltageMeasurementFilter(notNullVoltageCompSamples, 0);

        //Use slot 0
        canTalon.selectProfileSlot(0, 0);

        if (slaveTalons != null) {
            //Set up slaves.
            for (SlaveTalon slave : slaveTalons) {
                slave.setMaster(port, enableBrakeMode, currentLimit,
                        enableVoltageComp ? notNullVoltageCompSamples : null, PDP, voltagePerCurrentLinReg);
            }
        }

        if (slaveVictors != null) {
            //Set up slaves.
            for (SlaveVictor slave : slaveVictors) {
                slave.setMaster(canTalon, enableBrakeMode,
                        enableVoltageComp ? notNullVoltageCompSamples : null);
            }
        }

        if (slaveSparks != null){
            for(SlaveSparkMax slave :slaveSparks){
                slave.setMasterPhoenix(port, enableBrakeMode);
            }
        }
    }

    /**
     * Disables the motor, if applicable.
     */
    @Override
    public void disable() {
        canTalon.set(ControlMode.Disabled, 0);
    }


    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    public void setPercentVoltage(double percentVoltage) {
        //Warn the user if they're setting Vbus to a number that's outside the range of values.
        if (Math.abs(percentVoltage) > 1.0) {
            Shuffleboard.addEventMarker("WARNING: YOU ARE CLIPPING MAX PERCENT VBUS AT " + percentVoltage, this.getClass().getSimpleName(), EventImportance.kNormal);
            //Logger.addEvent("WARNING: YOU ARE CLIPPING MAX PERCENT VBUS AT " + percentVoltage, this.getClass());
            percentVoltage = Math.signum(percentVoltage);
        }

        setpoint = percentVoltage;

        canTalon.set(ControlMode.PercentOutput, percentVoltage);
    }

    /**
     * @return The gear this subsystem is currently in.
     */
    @Override
    @Log
    public int getGear() {
        return currentGearSettings.gear;
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

        //Set max voltage
        canTalon.configPeakOutputForward(currentGearSettings.fwdPeakOutputVoltage / 12., 0);
        canTalon.configPeakOutputReverse(currentGearSettings.revPeakOutputVoltage / 12., 0);

        //Set min voltage
        canTalon.configNominalOutputForward(currentGearSettings.fwdNominalOutputVoltage / 12., 0);
        canTalon.configNominalOutputReverse(currentGearSettings.revNominalOutputVoltage / 12., 0);

        if (currentGearSettings.rampRate != null) {
            //Set ramp rate, converting from volts/sec to seconds until 12 volts.
            canTalon.configClosedloopRamp(1 / (currentGearSettings.rampRate / 12.), 0);
            canTalon.configOpenloopRamp(1 / (currentGearSettings.rampRate / 12.), 0);
        } else {
            canTalon.configClosedloopRamp(0, 0);
            canTalon.configOpenloopRamp(0, 0);
        }

        //Set PID stuff
        //Slot 0 velocity gains. We don't set F yet because that changes based on setpoint.
        canTalon.config_kP(0, currentGearSettings.kP, 0);
        canTalon.config_kI(0, currentGearSettings.kI, 0);
        canTalon.config_kD(0, currentGearSettings.kD, 0);
    }

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param nativeUnits A distance native units as measured by the encoder.
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    @Override
    public double encoderToFeet(double nativeUnits) {
        if (encoderCPR == null) {
            return Double.NaN;
        }
        return nativeUnits / (encoderCPR * 4) * postEncoderGearing * feetPerRotation;
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
        if (encoderCPR == null) {
            return Double.NaN;
        }
        return feet / feetPerRotation * (encoderCPR * 4) / postEncoderGearing;
    }

    /**
     * Converts the velocity read by the talon's getVelocity() method to the FPS of the output shaft. Note this DOES
     * account for post-encoder gearing.
     *
     * @param encoderReading The velocity read from the encoder with no conversions.
     * @return The velocity of the output shaft, in FPS, when the encoder has that reading, or null if no encoder CPR
     * was given.
     */
    @Override
    public double encoderToFPS(double encoderReading) {
        RPS = nativeToRPS(encoderReading);
        if (RPS == null) {
            return Double.NaN;
        }
        return RPS * postEncoderGearing * feetPerRotation;
    }

    /**
     * Converts from the velocity of the output shaft to what the talon's getVelocity() method would read at that
     * velocity. Note this DOES account for post-encoder gearing.
     *
     * @param FPS The velocity of the output shaft, in FPS.
     * @return What the raw encoder reading would be at that velocity, or null if no encoder CPR was given.
     */
    @Override
    public double FPSToEncoder(double FPS) {
        return RPSToNative((FPS / postEncoderGearing) / feetPerRotation);
    }

    /**
     * Convert from CANTalon native velocity units to output rotations per second. Note this DOES NOT account for
     * post-encoder gearing.
     *
     * @param nat A velocity in CANTalon native units.
     * @return That velocity in RPS, or null if no encoder CPR was given.
     */
    @Contract(pure = true)
    @Nullable
    @Override
    public Double nativeToRPS(double nat) {
        if (encoderCPR == null) {
            return null;
        }
        return (nat / (encoderCPR * 4)) * 10; //4 edges per count, and 10 100ms per second.
    }

    /**
     * Convert from output RPS to the CANTalon native velocity units. Note this DOES NOT account for post-encoder
     * gearing.
     *
     * @param RPS The RPS velocity you want to convert.
     * @return That velocity in CANTalon native units, or null if no encoder CPR was given.
     */
    @Contract(pure = true)
    @Override
    public double RPSToNative(double RPS) {
        if (encoderCPR == null) {
            return Double.NaN;
        }
        return (RPS / 10) * (encoderCPR * 4); //4 edges per count, and 10 100ms per second.
    }

    /**
     * @return Total ticks travelled for debug purposes
     */
    @Override
    public double encoderPosition() {
        return canTalon.getSelectedSensorPosition();
    }

    /**
     * Set a position setpoint for the Talon.
     *
     * @param feet An absolute position setpoint, in feet.
     */
    @Override
    public void setPositionSetpoint(double feet) {
        setpoint = feet;
        nativeSetpoint = feetToEncoder(feet);
        canTalon.config_kF(0, 0);
        canTalon.set(ControlMode.Position, nativeSetpoint, DemandType.ArbitraryFeedForward,
                currentGearSettings.feedForwardCalculator.ks / 12.);
    }

    /**
     * @return Ticks per 100ms for debug purposes
     */
    @Override
    public double encoderVelocity() {
        return canTalon.getSelectedSensorVelocity();
    }

    /**
     * Get the velocity of the CANTalon in FPS.
     *
     * @return The CANTalon's velocity in FPS, or null if no encoder CPR was given.
     */
    @NotNull
    @Override
    public Double getVelocity() {
        return encoderToFPS(canTalon.getSelectedSensorVelocity(0));
    }

    /**
     * Set the velocity for the motor to go at.
     *
     * @param velocity the desired velocity, on [-1, 1].
     */
    @Override
    public void setVelocity(double velocity) {
        if (currentGearSettings.maxSpeed != null) {
            setVelocityFPS(velocity * currentGearSettings.maxSpeed);
        } else {
            setPercentVoltage(velocity);
        }
    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    @Override
    public void setVelocityFPS(double velocity) {
        nativeSetpoint = FPSToEncoder(velocity);
        setpoint = velocity;
        canTalon.config_kF(0, 0, 0);
        canTalon.set(ControlMode.Velocity, nativeSetpoint, DemandType.ArbitraryFeedForward,
                currentGearSettings.feedForwardCalculator.calculate(velocity) / 12.);
    }

    /**
     * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in velocity mode.
     *
     * @return The closed-loop error in FPS, or null if no encoder CPR was given.
     */
    @Log
    @Override
    public double getError() {
        if (canTalon.getControlMode().equals(ControlMode.Velocity)) {
            return encoderToFPS(canTalon.getClosedLoopError(0));
        } else {
            return encoderToFeet(canTalon.getClosedLoopError(0));
        }
    }

    /**
     * Get the current velocity setpoint of the Talon in FPS, the position setpoint in feet
     *
     * @return The setpoint in sensible units for the current control mode.
     */
    @Nullable
    @Log
    @Override
    public Double getSetpoint() {
        return setpoint;
    }

    /**
     * Get the voltage the Talon is currently drawing from the PDP.
     *
     * @return Voltage in volts.
     */
    @Log
    @Override
    public double getOutputVoltage() {
        return canTalon.getMotorOutputVoltage();
    }

    /**
     * Get the voltage available for the Talon.
     *
     * @return Voltage in volts.
     */
    @Log
    @Override
    public double getBatteryVoltage() {
        return canTalon.getBusVoltage();
    }

    /**
     * Get the current the Talon is currently drawing from the PDP.
     *
     * @return Current in amps.
     */
    @Log
    @Override
    public double getOutputCurrent() {
        return canTalon.getSupplyCurrent();
    }

    /**
     * Get the current control mode of the Talon. Please don't use this for anything other than logging.
     *
     * @return Control mode as a string.
     */
    @Override
    public String getControlMode() {
        return canTalon.getControlMode().name();
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
    public SimpleMotorFeedforward getCurrentGearFeedForward(){
        return currentGearSettings.feedForwardCalculator;
    }

    /**
     * @return the position of the talon in feet, or null of inches per rotation wasn't given.
     */
    @Override
    public Double getPositionFeet() {
        return encoderToFeet(canTalon.getSelectedSensorPosition(0));
    }

    /**
     * Resets the position of the Talon to 0.
     */
    @Override
    public void resetPosition() {
        canTalon.setSelectedSensorPosition(0, 0, 0);
    }

    /**
     * Get the status of the forwards limit switch.
     *
     * @return True if the forwards limit switch is closed, false if it's open or doesn't exist.
     */
    @Override
    public boolean getFwdLimitSwitch() {
        return fwdLimitSwitchNormallyOpen == canTalon.getSensorCollection().isFwdLimitSwitchClosed();
    }

    /**
     * Get the status of the reverse limit switch.
     *
     * @return True if the reverse limit switch is closed, false if it's open or doesn't exist.
     */
    @Override
    public boolean getRevLimitSwitch() {
        return revLimitSwitchNormallyOpen == canTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    @Override
    public boolean isInhibitedForward() {
        canTalon.getFaults(faults);
        return faults.ForwardLimitSwitch;
    }

    @Override
    public boolean isInhibitedReverse() {
        canTalon.getFaults(faults);
        return faults.ReverseLimitSwitch;
    }

    @Override
    public String configureLogName() {
        return name;
    }
}
