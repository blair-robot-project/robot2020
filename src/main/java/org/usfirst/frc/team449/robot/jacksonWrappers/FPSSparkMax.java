package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.revrobotics.*;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FPSSparkMax implements FPSSmartMotor {

    /**
     * REV brushless controller object
     */
    private CANSparkMax spark;

    /**
     * REV provided encoder object
     */
    private CANEncoder canEncoder;

    /**
     * REV provided PID Controller
     */
    private CANPIDController pidController;

    /**
     * The control mode of the motor
     */
    private ControlType currentControlMode;

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
     * A list of all the gears this robot has and their settings.
     */
    @NotNull
    private final Map<Integer, PerGearSettings> perGearSettings;

    /**
     * Forward limit switch object
     */
    private CANDigitalInput forwardLimitSwitch;

    /**
     * Reverse limit switch object
     */
    private CANDigitalInput reverseLimitSwitch;

    /**
     * The talon's name, used for logging purposes.
     */
    @NotNull
    private final String name;

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
    @Log
    private double nativeSetpoint;

    /**
     * Create a new SPARK MAX Controller
     * @param port                       CAN port of this Talon.
     * @param name                       The talon's name, used for logging purposes. Defaults to talon_portnum
     * @param reverseOutput              Whether to reverse the output.
     * @param enableBrakeMode            Whether to brake or coast when stopped.
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
     * @param perGearSettings            The settings for each gear this motor has. Can be null to use default values
     *                                   and gear # of zero. Gear numbers can't be repeated.
     * @param startingGear               The gear to start in. Can be null to use startingGearNum instead.
     * @param startingGearNum            The number of the gear to start in. Ignored if startingGear isn't null.
     *                                   Defaults to the lowest gear.
     * @param statusFrameRatesMillis     The update rates, in millis, for each of the Talon status frames.
     * @param controlFrameRateMillis    The update rate, in milliseconds, for each of the control frame.
     */
    @JsonCreator
    public FPSSparkMax(@JsonProperty(required = true) int port,
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
                       @Nullable final Map<CANSparkMax.PeriodicFrame, Integer> statusFrameRatesMillis,
                       @Nullable final Integer controlFrameRateMillis,
                       @Nullable List<SlaveSparkMax> slaveSparks) {
        spark = new CANSparkMax(port, CANSparkMaxLowLevel.MotorType.kBrushless);
        canEncoder = spark.getEncoder();
        pidController = spark.getPIDController();

        //Set the name to the given one or to talon_portnum
        this.name = name != null ? name : ("talon_" + port);
        //Set this to false because we only use reverseOutput for slaves.
        spark.setInverted(reverseOutput);
        //Set brake mode
        spark.setIdleMode(enableBrakeMode ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
        //Reset the position
        resetPosition();

        //Set frame rates
        if (controlFrameRateMillis != null) {
            // Must be between 1 and 100 ms.
            spark.setControlFramePeriodMs(controlFrameRateMillis);
        }

        if (statusFrameRatesMillis != null) {
            for (CANSparkMaxLowLevel.PeriodicFrame frame : statusFrameRatesMillis.keySet()) {
                spark.setPeriodicFramePeriod(frame, statusFrameRatesMillis.get(frame));
            }
        }

        this.PDP = PDP;


        this.feetPerRotation = feetPerRotation != null ? feetPerRotation : 1;

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
        //Set up gear-based settings.
        setGear(currentGear);
        //postEncoderGearing defaults to 1
        this.postEncoderGearing = postEncoderGearing != null ? postEncoderGearing : 1.;

        this.encoderCPR = canEncoder.getCountsPerRevolution();

        //Only enable the limit switches if it was specified if they're normally open or closed.
        if (fwdLimitSwitchNormallyOpen != null) {
            if (remoteLimitSwitchID != null) {
                //set CANDigitalInput to other limit switch
                forwardLimitSwitch = new CANSparkMax(remoteLimitSwitchID, CANSparkMaxLowLevel.MotorType.kBrushless)
                        .getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
            } else {
                forwardLimitSwitch = spark.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
            }
            this.fwdLimitSwitchNormallyOpen = fwdLimitSwitchNormallyOpen;
        } else {
            forwardLimitSwitch = spark.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
            forwardLimitSwitch.enableLimitSwitch(false);
            this.fwdLimitSwitchNormallyOpen = true;
        }
        if (revLimitSwitchNormallyOpen != null) {
            if (remoteLimitSwitchID != null) {
                reverseLimitSwitch = new CANSparkMax(remoteLimitSwitchID, CANSparkMaxLowLevel.MotorType.kBrushless)
                        .getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            } else {
                reverseLimitSwitch = spark.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            }
            this.revLimitSwitchNormallyOpen = revLimitSwitchNormallyOpen;
        } else {
            reverseLimitSwitch = spark.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
            reverseLimitSwitch.enableLimitSwitch(false);
            this.revLimitSwitchNormallyOpen = true;
        }

        if (fwdSoftLimit != null) {
            spark.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, fwdSoftLimit.floatValue());
        }
        if (revSoftLimit != null) {
            spark.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, revSoftLimit.floatValue());
        }

        //Set the current limit if it was given
        if (currentLimit != null) {
            spark.setSmartCurrentLimit(currentLimit);
        }

        if(enableVoltageComp){
            spark.enableVoltageCompensation(12);
        } else {
            spark.disableVoltageCompensation();
        }

        if (slaveSparks != null) {
            //Set up slaves.
            for (SlaveSparkMax slave : slaveSparks) {
                slave.setMasterSpark(spark, enableBrakeMode);
            }
        }

        spark.burnFlash();
    }


    @Override
    public void disable() {
        spark.disable();
    }

    @Override
    public void setPercentVoltage(double percentVoltage){
        currentControlMode = ControlType.kVoltage;
        //Warn the user if they're setting Vbus to a number that's outside the range of values.
        if (Math.abs(percentVoltage) > 1.0) {
            Shuffleboard.addEventMarker("WARNING: YOU ARE CLIPPING MAX PERCENT VBUS AT " + percentVoltage, this.getClass().getSimpleName(), EventImportance.kNormal);
            //Logger.addEvent("WARNING: YOU ARE CLIPPING MAX PERCENT VBUS AT " + percentVoltage, this.getClass());
            percentVoltage = Math.signum(percentVoltage);
        }

        setpoint = percentVoltage;

        spark.set(percentVoltage);
    }

    @Override
    public int getGear() {
        return currentGearSettings.gear;
    }

    @Override
    public void setGear(int gear) {
        //Set the current gear
        currentGearSettings = perGearSettings.get(gear);

        //note, no current limiting

        if (currentGearSettings.rampRate != null) {
            //Set ramp rate, converting from volts/sec to seconds until 12 volts.
            spark.setClosedLoopRampRate(1 / (currentGearSettings.rampRate / 12.));
            spark.setOpenLoopRampRate(1 / (currentGearSettings.rampRate / 12.));
        } else {
            spark.setClosedLoopRampRate(0);
            spark.setOpenLoopRampRate(0);
        }

        pidController.setP(currentGearSettings.kP, 0);
        pidController.setI(currentGearSettings.kI, 0);
        pidController.setD(currentGearSettings.kD, 0);

        spark.burnFlash();
    }

    /**
     * Convert from native units read by an encoder to feet moved. Note this DOES account for post-encoder gearing.
     *
     * @param revs revolutions measured by the encoder
     * @return That distance in feet, or null if no encoder CPR was given.
     */
    @Override
    public double encoderToFeet(double revs) {
        return revs * feetPerRotation * postEncoderGearing;
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
        return feet / feetPerRotation / postEncoderGearing;
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
     * @param nat A velocity in RPM
     * @return That velocity in RPS
     */
    @Contract(pure = true)
    @Override
    public Double nativeToRPS(double nat) {
        return nat / 60.;
    }

    /**
     * Convert from output RPS to the CANTalon native velocity units. Note this DOES NOT account for post-encoder
     * gearing.
     *
     * @param RPS The RPS velocity you want to convert.
     * @return That velocity in RPM
     */
    @Contract(pure = true)
    @Override
    public double RPSToNative(double RPS) {
        return RPS * 60.;
    }

    /**
     * @return Total revolutions for debug purposes
     */
    @Override
    public double encoderPosition() {
        return canEncoder.getPosition();
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
        pidController.setFF(currentGearSettings.feedForwardCalculator.ks / 12.);
        pidController.setReference(nativeSetpoint, ControlType.kPosition);
    }

    /**
     * @return Current RPM for debug purposes
     */
    @Override
    @Log
    public double encoderVelocity() {
        return canEncoder.getVelocity();
    }

    /**
     * Get the velocity of the CANTalon in FPS.
     *
     * @return The CANTalon's velocity in FPS, or null if no encoder CPR was given.
     */
    @Log
    public Double getVelocity() {
        return encoderToFPS(canEncoder.getVelocity());
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
        currentControlMode = ControlType.kVelocity;
        nativeSetpoint = FPSToEncoder(velocity);
        setpoint = velocity;
        pidController.setFF(currentGearSettings.feedForwardCalculator.calculate(velocity) / 12.);
        pidController.setReference(nativeSetpoint, ControlType.kVelocity);
    }

    @Override
    public double getError() {
        //but how though
        return 0;
    }

    @Nullable
    @Override
    public Double getSetpoint() {
        return setpoint;
    }

    @Override
    public double getOutputVoltage() {
        return spark.getAppliedOutput();
    }

    @Override
    public double getBatteryVoltage() {
        return spark.getBusVoltage();
    }

    @Override
    public double getOutputCurrent() {
        return spark.getOutputCurrent();
    }

    @Override
    public String getControlMode() {
        return currentControlMode.name();
    }

    @Override
    public void setGearScaledVelocity(double velocity, int gear) {
        if (currentGearSettings.maxSpeed != null) {
            setVelocityFPS(currentGearSettings.maxSpeed * velocity);
        } else {
            setPercentVoltage(velocity);
        }
    }

    @Override
    public void setGearScaledVelocity(double velocity, Shiftable.gear gear) {
        setGearScaledVelocity(velocity, gear.getNumVal());
    }

    @Override
    public SimpleMotorFeedforward getCurrentGearFeedForward() {
        return currentGearSettings.feedForwardCalculator;
    }

    @Override
    public Double getPositionFeet() {
        return encoderToFeet(canEncoder.getPosition());
    }

    @Override
    public void resetPosition() {
        canEncoder.setPosition(0);
    }

    @Override
    public boolean getFwdLimitSwitch() {
        return forwardLimitSwitch.get();
    }

    @Override
    public boolean getRevLimitSwitch() {
        return reverseLimitSwitch.get();
    }

    @Override
    public boolean isInhibitedForward() {
        return spark.getFault(CANSparkMax.FaultID.kHardLimitFwd);
    }

    @Override
    public boolean isInhibitedReverse() {
        return spark.getFault(CANSparkMax.FaultID.kHardLimitRev);
    }

    @Override
    public String configureLogName() {
        return name;
    }
}
