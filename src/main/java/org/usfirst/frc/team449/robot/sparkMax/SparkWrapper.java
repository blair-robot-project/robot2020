package org.usfirst.frc.team449.robot.sparkMax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.revrobotics.*;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.generalInterfaces.smartMotor.PerGearSettings;
import org.usfirst.frc.team449.robot.generalInterfaces.smartMotor.SmartMotorBase;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.usfirst.frc.team449.robot.util.Util.defaultIfNull;

/**
 * todo find out if wpi's Spark or revrobotics' SparkMax should be used. Probably the latter
 * todo also, make sure the slot is 0 for everything
 * Component wrapper on the SPARK MAX motor, with unit conversions to/from FPS built in. Every non-unit-conversion
 * in this class takes arguments in post-gearing FPS.
 *
 * @see FPSTalon
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SparkWrapper extends SmartMotorBase {
    /**
     * The SPARK-MAX that this class is a wrapper on
     */
    @NotNull
    protected final SparkAdapter canSpark;
    /**
     * Forward limit switch
     */
    protected final CANDigitalInput fwdLimitSwitch;
    /**
     * Reverse limit switch
     */
    protected final CANDigitalInput revLimitSwitch;
//    /**
//     * A cache for the results of querying status using {@link SparkWithMP#getMotionProfileStatus(MotionProfileStatus)}.
//     */
//    @NotNull
//    private final MotionProfileStatus motionProfileStatus;
    /**
     * The encoder that records everything. I'm assuming
     * we don't use analog (or hall effect) because the
     * constructor changes the type to quadrature if it is absolute or relative
     */
    private final CANEncoder encoder;

    /**
     * Default constructor.
     *
     * @param port                       CAN port of this Spark.
     * @param name                       The motorController's name, used for logging purposes. Defaults to talon_portnum
     * @param reverseOutput              Whether to reverse the output.
     * @param enableBrakeMode            Whether to brake or coast when stopped.
     * @param voltagePerCurrentLinReg    The component for doing linear regression to find the resistance.
     * @param PDP                        The PDP this Spark is connected to.
     * @param fwdLimitSwitchNormallyOpen Whether the forward limit switch is normally open or closed. If this is null,
     *                                   the forward limit switch is disabled.
     * @param revLimitSwitchNormallyOpen Whether the reverse limit switch is normally open or closed. If this is null,
     *                                   the reverse limit switch is disabled.
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
     *                                   null if there is no encoder attached to this Spark.
     *                                   todo ensure that it can take both a CANAnalog sensor and a CANEncoder
     * @param encoderCPR                 The counts per rotation of the encoder on this Spark. Can be null if
     *                                   feedbackDevice is, but otherwise must have a value.
     * @param reverseSensor              Whether or not to reverse the reading from the encoder on this Spark. Ignored
     *                                   if feedbackDevice is null. Defaults to false.
     * @param perGearSettings            The settings for each gear this motor has. Can be null to use default values
     *                                   and gear # of zero. Gear numbers can't be repeated.
     * @param startingGear               The gear to start in. Can be null to use startingGearNum instead. // TODO Can we use FunctionalJava's Either or something?
     * @param startingGearNum            The number of the gear to start in. Ignored if startingGear isn't null.
     *                                   Defaults to the lowest gear.
     * @param minNumPointsInBottomBuffer The minimum number of points that must be in the MP buffer before starting a
     *                                   profile. Defaults to 20.
     * @param updaterProcessPeriodSecs   The period for the Notifier that loads the next MP point if the Spark has
     *                                   attained the current one. Defaults to 0.005. TODO: Default should probably be much smaller
     * @param statusFrameRatesMillis     The update rates, in millis, for each of the Spark status frames.
     * @param controlFrameRateMillis     The update rate, in milliseconds, for each of the control frame.
     */
    @JsonCreator
    public SparkWrapper(@JsonProperty(required = true) final int port,
                        @JsonProperty(required = true) final boolean enableBrakeMode,
                        @NotNull @JsonProperty(required = true) final RunningLinRegComponent voltagePerCurrentLinReg,
                        @NotNull @JsonProperty(required = true) final Integer encoderCPR,
                        @NotNull @JsonProperty(required = true) final PDP PDP,
                        final boolean reverseOutput,
                        final boolean reverseSensor,
                        final boolean enableVoltageComp,
                        @Nullable final String name,
                        @Nullable final Boolean fwdLimitSwitchNormallyOpen,
                        @Nullable final Boolean revLimitSwitchNormallyOpen,
                        @Nullable final Double fwdSoftLimit,
                        @Nullable final Double revSoftLimit,
                        @Nullable final Double postEncoderGearing,
                        @Nullable final Double feetPerRotation,
                        @Nullable final Integer currentLimit,
                        @Nullable final Integer voltageCompSamples,
                        @Nullable final EncoderType feedbackDevice,
                        @Nullable final List<PerGearSettings> perGearSettings,
                        @Nullable final Shiftable.gear startingGear,
                        @Nullable final Integer startingGearNum,
                        @Nullable final Integer minNumPointsInBottomBuffer,
                        @Nullable final Double updaterProcessPeriodSecs,
                        @Nullable final Map<CANSparkMax.PeriodicFrame, Integer> statusFrameRatesMillis,
                        @Nullable final Integer controlFrameRateMillis /*todo figure out if this is right; there doesn't seem to be any use of it in the map anyways.*/) {
        super(
                port,
                PDP,
                feedbackDevice != null ? encoderCPR : null,
                Objects.requireNonNullElse(postEncoderGearing, null), // TODO Pick one of these and stick with it.
                defaultIfNull(feetPerRotation, 1),
                defaultIfNull(updaterProcessPeriodSecs, 0.005),
                name,
                voltagePerCurrentLinReg,
                defaultIfNull(fwdLimitSwitchNormallyOpen, true),
                defaultIfNull(revLimitSwitchNormallyOpen, true),
                minNumPointsInBottomBuffer,
                perGearSettings,
                startingGear,
                startingGearNum);

        this.canSpark = new SparkAdapter(port, CANSparkMaxLowLevel.MotorType.kBrushless);
//        this.canSpark = new SparkWithMP(port, CANSparkMaxLowLevel.MotorType.kBrushless, this.updaterProcessPeriodSecs);

        //        this.motionProfileStatus = new MotionProfileStatus();

        //Set this to false because we only use reverseOutput for slaves.
        this.canSpark.setInverted(reverseOutput);
        //Set brake mode
        this.canSpark.setIdleMode(enableBrakeMode ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);

        //Make CAN calls non-blocking
        this.canSpark.setCANTimeout(0);

        //Set frame rates
        if (controlFrameRateMillis != null) {
            // Must be between 1 and 100 ms.
            this.canSpark.setControlFramePeriodMs(controlFrameRateMillis);
        }

        if (statusFrameRatesMillis != null) {
            for (final CANSparkMaxLowLevel.PeriodicFrame frame : statusFrameRatesMillis.keySet()) {
                this.canSpark.setPeriodicFramePeriod(frame, statusFrameRatesMillis.get(frame));
            }
        }

        //Initialize
        this.timeMPStatusLastRead = 0;


        //Only enable the limit switches if it was specified if they're normally open or closed.
        if (fwdLimitSwitchNormallyOpen != null) {
            this.fwdLimitSwitch = this.canSpark.
                    getForwardLimitSwitch(fwdLimitSwitchNormallyOpen ?
                            CANDigitalInput.LimitSwitchPolarity.kNormallyOpen :
                            CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);

            //is this the right one?
            //canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, !fwdLimitSwitchNormallyOpen);

            /*original:
            canSpark.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                    fwdLimitSwitchNormallyOpen ? LimitSwitchNormal.NormallyOpen : LimitSwitchNormal.NormallyClosed, 0);*/
        } else {
            //canSpark.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
            this.fwdLimitSwitch = this.canSpark.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            this.fwdLimitSwitch.enableLimitSwitch(true);
        }
        if (revLimitSwitchNormallyOpen != null) {
            this.revLimitSwitch = this.canSpark.
                    getReverseLimitSwitch(revLimitSwitchNormallyOpen ?
                            CANDigitalInput.LimitSwitchPolarity.kNormallyOpen :
                            CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            //is this the right one?
            //canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, !revLimitSwitchNormallyOpen);

            /*canSpark.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                    revLimitSwitchNormallyOpen ? LimitSwitchNormal.NormallyOpen : LimitSwitchNormal.NormallyClosed, 0);*/
        } else {
            /*canSpark.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);*/
            this.revLimitSwitch = this.canSpark.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            this.revLimitSwitch.enableLimitSwitch(true);
        }

        //Set up the feedback device if it exists.
        if (feedbackDevice != null) {
            //CTRE encoderencoder use RPM instead of native units, and can be used as QuadEncoders, so we switch them to avoid
            //having to support RPM.
            //todo check if only encoders or analogs are also used
            /*if (feedbackDevice.equals(FeedbackDevice.CTRE_MagEncoder_Absolute) ||
                    feedbackDevice.equals(FeedbackDevice.CTRE_MagEncoder_Relative)) {
                motorController.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
            } else {
                canSpark.configSelectedFeedbackSensor(feedbackDevice, 0, 0);
            }*/
            //T:I'm guessing we want quadrature instead of hall effect
            this.encoder = new CANEncoder(this.getSpark(), feedbackDevice, encoderCPR);
            this.encoder.setInverted(reverseSensor);

            //Only enable the software limits if they were given a value and there's an encoder.
            if (fwdSoftLimit != null) {
                this.canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
                this.canSpark.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float) this.feetToEncoder(fwdSoftLimit));
            } else {
                this.canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
            }
            if (revSoftLimit != null) {
                this.canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
                this.canSpark.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float) this.feetToEncoder(revSoftLimit));
            } else {
                this.canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
            }
        } else {
            // This assumes that the kNoSensor causes the cpr to be ignored.
            this.encoder = new CANEncoder(this.canSpark, EncoderType.kNoSensor, 0);
        }
        this.canSpark.getPIDController().setFeedbackDevice(this.encoder);

        //Set the current limit if it was given
        if (currentLimit != null) {
            //todo either change the parameters to include a stallLimit too,
            // or just do the one parameter setSCLimit method
            // TODO: FPSTalon isn't using the Talon's peak current limiting, so this code is fine as is.
            this.canSpark.setSmartCurrentLimit(currentLimit);
        } else {
            //If we don't have a current limit, disable current limiting.
            this.canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
            this.canSpark.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
        }

        //Enable or disable voltage comp
        this.canSpark.enableVoltageCompensation(enableVoltageComp ? VOLTAGE_COMPENSATION : 0);

        // TODO: Don't think this is possible.
        // canSpark.configVoltageMeasurementFilter(voltageCompSamples != null ? voltageCompSamples : 32, 0);

        // TODO: I don't think Spark supports differential control, either.
        // canSpark.selectProfileSlot(0, 0);

        // TODO: Slaves are not implemented
        /* I doubt these will be used
        if (slaveTalons != null) {
            //Set up slaves.
            for (SlaveTalon slave : slaveTalons) {
                slave.setMaster(port, enableBrakeMode, currentLimit, PDP, voltagePerCurrentLinReg.clone());
                Logger.addLoggable(slave);
            }
        }

        if (slaveVictors != null) {
            //Set up slaves.
            for (SlaveVictor slave : slaveVictors) {
                slave.setMaster(port, enableBrakeMode);
            }
        }*/
    }

    public CANSparkMax getSpark() {
        return this.canSpark;
    }

    /**
     * Set the motor output voltage to a given percent of available voltage.
     *
     * @param percentVoltage percent of total voltage from [-1, 1]
     */
    @Override
    public void setPercentVoltage(double percentVoltage) {
        //Warn the user if they're setting Vbus to a number that's outside the range of values.
        if (Math.abs(percentVoltage) > 1.0) {
            Shuffleboard.addEventMarker("WARNING: YOU ARE CLIPPING MAX PERCENT VBUS AT " + percentVoltage, this.getClass().getSimpleName(), EventImportance.kNormal);
            percentVoltage = Math.signum(percentVoltage);
        }

        System.out.println("**************************************************************setpercentvoltage: " + percentVoltage);
        this.canSpark.set(ControlType.kVoltage, percentVoltage);
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

        //Set max voltage
        //todo I am fairly sure this is the right method, but I'm not totally sure
        //  unfortunately, instead of having four methods for min and max forward
        //  and reverse outputs, it only has a range method with max forward output
        //  and min reverse output.
        //  Also, they recommend using the SPARK MAX GUI
        this.canSpark.getPIDController().setOutputRange(this.currentGearSettings.getRevPeakOutputVoltage() / 12., this.currentGearSettings.getFwdPeakOutputVoltage() / 12.);
        /*motorController.configPeakOutputForward(currentGearSettings.getFwdPeakOutputVoltage() / 12., 0);
        canSpark.configPeakOutputReverse(currentGearSettings.getRevPeakOutputVoltage() / 12., 0);*/

        //Set min voltage
        /*canSpark.configNominalOutputForward(currentGearSettings.getFwdNominalOutputVoltage() / 12., 0);
        canSpark.configNominalOutputReverse(currentGearSettings.getRevNominalOutputVoltage() / 12., 0);*/

        if (this.currentGearSettings.getRampRate() != null) {
            //Set ramp rate, converting from volts/sec to seconds until 12 volts.
            this.canSpark.setClosedLoopRampRate(1 / (this.currentGearSettings.getRampRate() / 12.));
            this.canSpark.setOpenLoopRampRate(1 / (this.currentGearSettings.getRampRate() / 12.));
        } else {
            this.canSpark.setClosedLoopRampRate(0);
            this.canSpark.setOpenLoopRampRate(0);
        }

        // No more motion magic...
//        //Set motion magic stuff
//        if (this.currentGearSettings.getMaxSpeed() != null) {
//            this.canSpark.getPIDController().setSmartMotionMaxVelocity(this.FPSToEncoder(this.currentGearSettings.getMaxSpeed()).intValue(), 0);
//            //We can convert accel the same way we do vel because both are per second.
//            this.canSpark.getPIDController().setSmartMotionMaxAccel(this.FPSToEncoder(this.currentGearSettings.getMotionMagicMaxAccel()).intValue(), 0);
//        }

        //Set PID stuff
        if (this.currentGearSettings.getMaxSpeed() != null) {
            //Slot 0 velocity gains. We don't set F yet because that changes based on setpoint.
            this.canSpark.getPIDController().setP(this.currentGearSettings.getkP(), 0);
            this.canSpark.getPIDController().setI(this.currentGearSettings.getkI(), 0);
            this.canSpark.getPIDController().setD(this.currentGearSettings.getkD(), 0);

            //We set the MP gains when loading a profile so no need to do it here.
        }
    }

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param nativeUnits A distance native units as measured by the encoder.
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    @Nullable
    @Contract(pure = true)
    protected Double encoderToFeet(final double nativeUnits) {
        if (this.encoderCPR == null) {
            return null;
        }
        return nativeUnits / (this.encoderCPR * 4) * this.postEncoderGearing * this.feetPerRotation;
    }

    /**
     * Convert a distance from feet to encoder reading in native units. Note this DOES account for post-encoder
     * gearing.
     *
     * @param feet A distance in feet.
     * @return That distance in native units as measured by the encoder, or null if no encoder CPR was given.
     */
    protected double feetToEncoder(final double feet) {
        if (this.encoderCPR == null) {
            return Double.NaN;
        }

        final double numRotations = feet / this.feetPerRotation;
        final int encoderPPR = this.encoderCPR * 4;
        return numRotations * encoderPPR / this.postEncoderGearing;
    }

    /**
     * Converts the velocity read by the motorController's getVelocity() method to the FPS of the output shaft. Note this DOES
     * account for post-encoder gearing.
     *
     * @param encoderReading The velocity read from the encoder with no conversions.
     * @return The velocity of the output shaft, in FPS, when the encoder has that reading, or null if no encoder CPR
     * was given.
     */
    @Nullable
    @Contract(pure = true)
    protected Double encoderToFPS(final double encoderReading) {
        this.RPS = this.nativeToRPS(encoderReading);
        if (this.RPS == null) {
            return null;
        }
        return this.RPS * this.postEncoderGearing * this.feetPerRotation;
    }

    /**
     * Converts from the velocity of the output shaft to what the motorController's getVelocity() method would read at that
     * velocity. Note this DOES account for post-encoder gearing.
     *
     * @param FPS The velocity of the output shaft, in FPS.
     * @return What the raw encoder reading would be at that velocity, or null if no encoder CPR was given.
     */
    protected double FPSToEncoder(final double FPS) {
        return this.RPSToNative((FPS / this.postEncoderGearing) / this.feetPerRotation);
    }

    /**
     * Convert from canSpark native velocity units to output rotations per second. Note this DOES NOT account for
     * post-encoder gearing.
     *
     * @param nat A velocity in canSpark native units.
     * @return That velocity in RPS, or null if no encoder CPR was given.
     */
    @Nullable
    @Contract(pure = true)
    private Double nativeToRPS(final double nat) {
        if (this.encoderCPR == null) {
            return null;
        }
        return (nat / (this.encoderCPR * 4)) * 10; //4 edges per count, and 10 100ms per second.
    }

    /**
     * Convert from output RPS to the canSpark native velocity units. Note this DOES NOT account for post-encoder
     * gearing.
     *
     * @param RPS The RPS velocity you want to convert.
     * @return That velocity in canSpark native units, or null if no encoder CPR was given.
     */
    @Contract(pure = true)
    private double RPSToNative(final double RPS) {
        if (this.encoderCPR == null) {
            return Double.NaN;
        }
        return (RPS / 10) * (this.encoderCPR * 4); //4 edges per count, and 10 100ms per second.
    }

    /**
     * Set a position setpoint for the Spark.
     *
     * @param feet An absolute position setpoint, in feet.
     */
    @Override
    public void setPositionSetpoint(final double feet) {
        this.nativeSetpoint = this.feetToEncoder(feet);

        this.canSpark.getPIDController().setFF(this.currentGearSettings.getFeedForwardCalculator().ks / 12., 0);
        this.canSpark.set(ControlType.kPosition, this.nativeSetpoint);
    }

    /**
     * Get the velocity of the canSpark in FPS.
     *
     * @return The canSpark's velocity in FPS, or null if no encoder CPR was given.
     */
    @Override
    @Nullable
    @Contract(pure = true)
    public Double getVelocity() {
        return this.encoderToFPS(this.canSpark.getEncoder().getVelocity());
    }

    /**
     * Set the velocity for the motor to go at.
     *
     * @param velocity the desired velocity, on [-1, 1].
     */
    @Override
    public void setVelocity(final double velocity) {
        System.out.println("**************************************************************setvelocity");
        if (this.currentGearSettings.getMaxSpeed() != null) {
            this.setVelocityFPS(velocity * this.currentGearSettings.getMaxSpeed());
        } else {
            this.setPercentVoltage(velocity);
        }
    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    protected void setVelocityFPS(final double velocity) {
        this.nativeSetpoint = this.FPSToEncoder(velocity);
        this.canSpark.getPIDController().setFF(this.nativeSetpoint * this.currentGearSettings.getFeedForwardCalculator().ks / 12., 0);
        this.canSpark.set(ControlType.kVelocity, this.nativeSetpoint);
    }

    /**
     * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in VELOCITY mode.
     *
     * @return The closed-loop error in FPS, or null if no encoder CPR was given.
     */
    @Override
    @Nullable
    @Contract(pure = true)
    public Double getError() {
        // TODO Doesn't seem like this is actually possible.
        //return this.encoderToFPS(this.canSpark.getPIDController().getSmartMotionAllowedClosedLoopError(0));

        return 0.;
    }

    /**
     * Get the current velocity setpoint of the Spark in FPS. WARNING: will give garbage if not in VELOCITY mode.
     *
     * @return The closed-loop velocity setpoint in FPS, or null if no encoder CPR was given.
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public Double getSetpoint() {
        return this.canSpark.getSetPoint();
    }

    /**
     * Get the voltage the Spark is currently supplying. WARNING: will give garbage if not in VOLTAGE mode.
     *
     * @return Voltage in volts.
     *
     * <p>
     * This method is only used in the current limiter for the climber and in logging methods,
     * so I think it's okay to leave it unimplemented for now in case this isn't correct.
     * (assuming that the setpoint is already the output voltage percent)
     */
    @Override
    @Contract(pure = true)
    public double getOutputVoltage() {
        return this.canSpark.getMotorOutputVoltage();
    }

    /**
     * Get the duty cycle with which the Spark is currently outputting.
     *
     * @return the fraction of each PWM cycle that the Spark is supplying voltage.
     */
    @Contract(pure = true)
    public double getOutputDutyCycle() {
        return this.canSpark.getAppliedOutput();
    }

    /**
     * Get the voltage available for the Spark.
     *
     * @return Voltage in volts.
     */
    @Override
    @Contract(pure = true)
    public double getBatteryVoltage() {
        return this.canSpark.getBusVoltage();
    }

    /**
     * Get the current the Spark is currently drawing from the PDP.
     *
     * @return Current in amps.
     */
    @Override
    @Contract(pure = true)
    public double getOutputCurrent() {
        return this.canSpark.getOutputCurrent();
    }

    /**
     * Get the current control mode of the Spark. Please don't use this for anything other than logging.
     *
     * @return Control mode as a string.
     */
    @Override
    @Contract(pure = true)
    public String getControlMode() {
        return String.valueOf(this.canSpark.getControlType());
    }

    /**
     * Enables the motor, if applicable.
     */
    @Override
    public void enable() {
        //Not a thing anymore
    }

    /**
     * Disables the motor, if applicable.
     */
    @Override
    public void disable() {
        this.canSpark.disable();
    }

    /**
     * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
     *
     * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given gear.
     * @param gear     The number of the gear to use the max speed from to scale the velocity.
     */
    @Override
    public void setGearScaledVelocity(final double velocity, final int gear) {
        if (this.currentGearSettings.getMaxSpeed() == null) {
            this.setPercentVoltage(velocity);
        } else {
            this.setVelocityFPS(perGearSettings.get(gear).getMaxSpeed() * velocity); // TODO
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
     * @return the position of the motorController in feet, or null if inches per rotation wasn't given.
     */
    @Override
    @Nullable
    @Contract(pure = true)
    public Double getPositionFeet() {
        return this.encoderToFeet(this.encoder.getPosition());
    }

    /**
     * Resets the position of the Talon to 0.
     */
    @Override
    public void resetPosition() {
        this.canSpark.setSelectedSensorPosition(0, 0, 0);
    }

    /**
     * Get the status of the forwards limit switch.
     *
     * @return {@literal true} if the forwards limit switch is closed; {@literal false} if it's open or doesn't exist.
     *
     * <p>
     * {@link CANDigitalInput#get()} also matches the behavior of {@link com.ctre.phoenix.motorcontrol.SensorCollection#isFwdLimitSwitchClosed()} in
     * ignoring whether it is enabled.
     */
    @Override
    @Contract(pure = true)
    public boolean getFwdLimitSwitch() {
        return this.fwdLimitSwitchNormallyOpen == this.fwdLimitSwitch.get();
    }

    /**
     * Get the status of the reverse limit switch.
     *
     * @return True if the reverse limit switch is closed, false if it's open or doesn't exist.
     */
    @Override
    @Contract(pure = true)
    public boolean getRevLimitSwitch() {
        return this.revLimitSwitchNormallyOpen == this.revLimitSwitch.get();
    }

//    /**
//     * A private utility method for updating motionProfileStatus with the current motion profile status. Makes sure that
//     * the status is only gotten once per tick, to avoid CAN traffic overload.
//     * <p>
//     * Analogous to FPSTalon#updateMotionProfileStatus() (not publicly accessible)
//     */
//    protected void updateMotionProfileStatus() {
//        if (this.timeMPStatusLastRead < Clock.currentTimeMillis()) {
//            this.canSpark.getMotionProfileStatus(this.motionProfileStatus);
//            this.timeMPStatusLastRead = Clock.currentTimeMillis();
//        }
//    }
//
//    /**
//     * Whether this motorController is ready to start running a profile.
//     *
//     * @return True if minNumPointsInBottomBuffer points have been loaded or the top buffer is empty, false otherwise.
//     */
//    @Override
//    public boolean readyForMP() {
//        this.updateMotionProfileStatus();
//        return this.motionProfileStatus.topBufferCnt == 0 || this.motionProfileStatus.btmBufferCnt >= this.minNumPointsInBottomBuffer;
//    }
//
//    /**
//     * Whether this motorController has finished running a profile.
//     *
//     * @return True if the active point in the motorController is the last point, false otherwise.
//     */
//    @Override
//    public boolean MPIsFinished() {
//        this.updateMotionProfileStatus();
//        return this.motionProfileStatus.isLast;
//    }
//
//    /**
//     * Reset all MP-related stuff, including all points loaded in both the API and bottom-level buffers.
//     */
//    private void clearMP() {
//        this.canSpark.clearMotionProfileHasUnderrun(0);
//        this.canSpark.clearMotionProfileTrajectories();
//    }
//
//    /**
//     * Starts running the loaded motion profile.
//     */
//    @Override
//    public void startRunningMP() {
//        System.out.println("SparkWrapper Started MP");
//        this.canSpark.setMP();
//    }
//
//    @Override
//    public void holdPositionMP() {
//        // Set the target position to be the current position.
//        this.canSpark.setReference(ControlType.kPosition, this.encoder.getPosition());
//    }
//
//    @Override
//    public void executeMPPoint(double pos, double vel, double acc) {
//        this.canSpark.setPointReference(pos, vel, acc);
//    }
//
//    /**
//     * Disables the controller and loads the given profile.
//     *
//     * @param data The profile to load.
//     */
//    @Override
//    public void loadProfile(final MotionProfileData data) {
//        //Reset the Spark
//        this.disable();
//        this.clearMP();
//
//        // TODO: (this code was copied from FPSTalon) This doesn't make sense since primitives are located on the stack.
//        //Declare this out here to avoid garbage collection
//        double feedforward;
//
//        //Set proper PID constants
//        if (data.isVelocityOnly()) {
//            this.canSpark.getPIDController().setP(0, 1);
//            this.canSpark.getPIDController().setI(0, 1);
//            this.canSpark.getPIDController().setD(0, 1);
//        } else {
//            if (data.isBackwards()) {
//                this.canSpark.getPIDController().setP(this.currentGearSettings.getMotionProfilePRev(), 1);
//                this.canSpark.getPIDController().setI(this.currentGearSettings.getMotionProfileIRev(), 1);
//                this.canSpark.getPIDController().setD(this.currentGearSettings.getMotionProfileDRev(), 1);
//            } else {
//                this.canSpark.getPIDController().setP(this.currentGearSettings.getMotionProfilePFwd(), 1);
//                this.canSpark.getPIDController().setI(this.currentGearSettings.getMotionProfileIFwd(), 1);
//                this.canSpark.getPIDController().setD(this.currentGearSettings.getMotionProfileDFwd(), 1);
//            }
//        }
//
//        this.canSpark.getPIDController().setFF(1023. / 12., 1);
//
//        //Only call position getter once
//        final double startPosition = data.resetPosition() ? 0 : defaultIfNull(this.getPositionFeet(), 0);
//
//        //Set point time
//        this.canSpark.configMotionProfileTrajectoryPeriod(data.getPointTimeMillis(), 0);
//
//        //Load in profiles
//        for (int i = 0; i < data.getData().length; ++i) {
//            final TrajectoryPoint point = new TrajectoryPoint();
//            //Have to set this so the Spark doesn't throw a null pointer. May be fixed in a future release.
//            point.timeDur = 0;
//
//            //Set parameters that are true for all points
//            point.profileSlotSelect0 = 1;        // gain selection, we always put MP gains in slot 1.
//
//            // Set all the fields of the profile point
//            point.position = defaultIfNull(this.feetToEncoder(startPosition + (data.getData()[i][0] * (data.isBackwards() ? -1 : 1))), 0);
//
//            feedforward = this.currentGearSettings.getFeedForwardComponent().calcMPVoltage(data.getData()[i][0],
//                    data.getData()[i][1], data.getData()[i][2]);
//            Logger.addEvent("VelPlusAccel: " + feedforward, this.getClass());
//            point.velocity = feedforward;
//
//            //Doing vel+accel shouldn't lead to impossible setpoints, so if it does, we log so we know to change either the profile or kA.
//            if (Math.abs(feedforward) > 12) {
//                System.out.println("Point " + Arrays.toString(data.getData()[i]) + " has an unattainable velocity+acceleration setpoint!");
//                Logger.addEvent("Point " + Arrays.toString(data.getData()[i]) + " has an unattainable velocity+acceleration setpoint!", this.getClass());
//            }
//            point.zeroPos = i == 0 && data.resetPosition(); // If it's the first point, set the encoder position to 0.
//            point.isLastPoint = (i + 1) == data.getData().length; // If it's the last point, isLastPoint = true
//
//            // Send the point to the Spark's buffer
//            this.canSpark.pushMPPoint(point);
//        }
//    }

//    /**
//     * Get the headers for the data this subsystem logs every loop.
//     *
//     * @return An N-length array of String labels for data, where N is the length of the Object[] returned by getData().
//     */
//    @NotNull
//    @Override
//    @Contract(pure = true)
//    public String[] getHeader() {
//        return new String[] {
//                "velocity",
//                "position",
//                "setpoint",
//                "error",
//                "battery_voltage",
//                "voltage",
//                "current",
//                "control_mode",
//                "gear",
//                "resistance"
//        };
//    }
//
//    /**
//     * Get the data this subsystem logs every loop.
//     *
//     * @return An N-length array of Objects, where N is the number of labels given by getHeader.
//     */
//    @Nullable
//    @Override
//    @Contract(pure = true)
//    public Object[] getData() {
//        if (this.voltagePerCurrentLinReg != null && this.PDP != null) {
//            this.voltagePerCurrentLinReg.addPoint(this.getOutputCurrent(), this.PDP.getVoltage() - this.getBatteryVoltage());
//        }
//        return new Object[] {
//                this.getVelocity(),
//                this.getPositionFeet(),
//                this.getSetpoint(),
//                this.getError(),
//                this.getBatteryVoltage(),
//                this.getOutputVoltage(),
//                this.getOutputCurrent(),
//                this.getControlMode(),
//                this.getGear(),
//                this.voltagePerCurrentLinReg == null ? null : -this.voltagePerCurrentLinReg.getSlope()
//        };
//    }
//
//    /**
//     * Get the name of this object.
//     *
//     * @return A string that will identify this object in the log file.
//     */
//    @NotNull
//    @Override
//    @Contract(pure = true)
//    public String getLogName() {
//        return this.name;
//    }

    @Override
    public String configureLogName() {
        return name;
    }

    @Override
    public LayoutType configureLayoutType() {
        return BuiltInLayouts.kGrid;
    }
}
