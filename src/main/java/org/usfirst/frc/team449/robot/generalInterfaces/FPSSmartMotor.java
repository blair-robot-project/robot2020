package org.usfirst.frc.team449.robot.generalInterfaces;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;
import org.usfirst.frc.team449.robot.jacksonWrappers.*;

import java.util.*;


/**
 * A motor with built in advanced capability featuring encoder, current limiting, and gear shifting support.
 * Also features built in FPS conversions.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public interface FPSSmartMotor extends SimpleMotor, Shiftable, Loggable {

    /**
     * Creates a new <b>SPARK MAX</b> or <b>FPS TALON</b> motor controller.
     *
     * @param port                       CAN port of this Talon.
     * @param name                       The talon's name, used for logging purposes. Defaults to talon_portnum
     * @param reverseOutput              Whether to reverse the output.
     * @param enableBrakeMode            Whether to brake or coast when stopped.
     * @param voltagePerCurrentLinReg    TALON-SPECIFIC. The component for doing linear regression to find the resistance.
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
     * @param voltageCompSamples         TALON-SPECIFIC. The number of 1-millisecond samples to use for voltage compensation. Defaults
     *                                   to 32.
     * @param feedbackDevice             TALON-SPECIFIC. The type of encoder used to measure the output velocity of this motor. Can be
     *                                   null if there is no encoder attached to this Talon.
     * @param encoderCPR                 TALON-SPECIFIC. The counts per rotation of the encoder on this Talon. Can be null if
     *                                   feedbackDevice is, but otherwise must have a value.
     * @param reverseSensor              TALON-SPECIFIC. Whether or not to reverse the reading from the encoder on this Talon. Ignored
     *                                   if feedbackDevice is null. Defaults to false.
     * @param perGearSettings            The settings for each gear this motor has. Can be null to use default values
     *                                   and gear # of zero. Gear numbers can't be repeated.
     * @param startingGear               The gear to start in. Can be null to use startingGearNum instead.
     * @param startingGearNum            The number of the gear to start in. Ignored if startingGear isn't null.
     *                                   Defaults to the lowest gear.
     * @param updaterProcessPeriodSecs   TALON-SPECIFIC. The period for the {@link Notifier} that moves points between the MP buffers, in
     *                                   seconds. Defaults to 0.005.
     * @param statusFrameRatesMillis     TALONThe update rates, in millis, for each of the Talon status frames.
     * @param controlFrameRateMillis     SPARK-SPECIFIC. The update rate, in milliseconds, each control frame.
     * @param controlFrameRatesMillis    TALON-SPECIFIC. The update rate, in milliseconds, for each of the control frame.
     * @param slaveTalons                TALON-SPECIFIC. The other {@link TalonSRX}s that are slaved to this one.
     * @param slaveVictors               TALON-SPECIFIC. The {@link com.ctre.phoenix.motorcontrol.can.VictorSPX}s that are slaved to
     *                                   this Talon.
     * @param slaveSparks                The Spark/Neo combinations slaved to this Talon.
     */
    @JsonCreator
    static FPSSmartMotor create(@JsonProperty(required = true) Type type,
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
                                // Talon-specific
                                @Nullable final Map<?, Integer> statusFrameRatesMillis,
                                @Nullable final Integer controlFrameRateMillis,
                                @Nullable final Map<ControlFrame, Integer> controlFrameRatesMillis,
                                @Nullable RunningLinRegComponent voltagePerCurrentLinReg,
                                @Nullable Integer voltageCompSamples,
                                @Nullable FeedbackDevice feedbackDevice,
                                @Nullable Integer encoderCPR,
                                @Nullable Boolean reverseSensor,
                                @Nullable Double updaterProcessPeriodSecs,
                                @Nullable List<SlaveTalon> slaveTalons,
                                @Nullable List<SlaveVictor> slaveVictors,
                                @Nullable List<SlaveSparkMax> slaveSparks
    ) {
        // The status frames map must be dealt with manually.
        var sparkStatusFramesMap = new HashMap<CANSparkMaxLowLevel.PeriodicFrame, Integer>();
        var talonStatusFramesMap = new HashMap<StatusFrameEnhanced, Integer>();

        if (statusFrameRatesMillis != null) {
            for (Object frame : statusFrameRatesMillis.keySet()) {
                if (frame instanceof String) {
                    // We can parse it ourselves.
                    String toBeParsed = "\"" + frame.toString() + "\"";
                    try {
                        if (type == Type.TALON) {
                            talonStatusFramesMap.put(new ObjectMapper().readValue(toBeParsed, StatusFrameEnhanced.class), statusFrameRatesMillis.get(frame));
                        } else if (type == Type.SPARKMAX) {
                            sparkStatusFramesMap.put(new ObjectMapper().readValue(toBeParsed, CANSparkMaxLowLevel.PeriodicFrame.class), statusFrameRatesMillis.get(frame));
                        }
                    } catch (Exception ex) {
                        System.out.println("Could not parse status frame rate key value + " + toBeParsed);
                        ex.printStackTrace();
                    }

                } else if (frame instanceof CANSparkMaxLowLevel.PeriodicFrame) {
                    if (type == Type.TALON)
                        throw new IllegalArgumentException("statusFrameRatesMillis contains key of type CANSparkMaxLowLevel.PeriodicFrame that will not work for FPSTalon");
                    sparkStatusFramesMap.put((CANSparkMaxLowLevel.PeriodicFrame) frame, statusFrameRatesMillis.get(frame));

                } else if (frame instanceof StatusFrameEnhanced) {
                    if (type == Type.SPARKMAX)
                        throw new IllegalArgumentException("statusFrameRatesMillis contains key of type StatusFrameEnhanced that will not work for SparkMax");
                    talonStatusFramesMap.put((StatusFrameEnhanced) frame, statusFrameRatesMillis.get(frame));

                } else {
                    throw new IllegalArgumentException("statusFrameRatesMillis contains key of unexpected type " + frame.getClass().getName());
                }
            }
        }

        switch (type) {
            case SPARKMAX:
                if (slaveTalons != null)
                    System.out.println("WARNING: Property slaveTalons is not supported for FPSSparkMax");
                if (slaveVictors != null)
                    System.out.println("WARNING: Property slaveTalons is not supported for FPSSparkMax");
                if (voltagePerCurrentLinReg != null)
                    System.out.println("WARNING: Property voltagePerCurrentLinReg is not supported for FPSSparkMax");
                if (encoderCPR != null)
                    System.out.println("WARNING: Property encoderCPR is not supported for FPSSparkMax");
                if (reverseSensor != null)
                    System.out.println("WARNING: Property reverseSensor is not supported for FPSSparkMax");
                if (voltageCompSamples != null)
                    System.out.println("WARNING: Property voltageCompSamples is not supported for FPSSparkMax");
                if (updaterProcessPeriodSecs != null)
                    System.out.println("WARNING: Property updaterProcessPeriodSecs is not supported for FPSSparkMax");
                if (controlFrameRateMillis != null)
                    System.out.println("WARNING: Property controlFrameRateMillis (RATE--singular) is not supported for FPSSparkMax");

                return new FPSSparkMax(port, name, reverseOutput, enableBrakeMode, PDP, fwdLimitSwitchNormallyOpen, revLimitSwitchNormallyOpen, remoteLimitSwitchID, fwdSoftLimit, revSoftLimit, postEncoderGearing, feetPerRotation, currentLimit, enableVoltageComp, perGearSettings, startingGear, startingGearNum, sparkStatusFramesMap, controlFrameRateMillis, slaveSparks);

            case TALON:
                if (reverseSensor == null)
                    throw new IllegalArgumentException("Property reverseSensor cannot be null for FPSTalon");
                if (controlFrameRatesMillis != null)
                    System.out.println("WARNING: Property controlFrameRatesMillis (RATESSSS--plural) is not supported for FPSTalon");

                return new FPSTalon(port, name, reverseOutput, enableBrakeMode, voltagePerCurrentLinReg, PDP, fwdLimitSwitchNormallyOpen, revLimitSwitchNormallyOpen, remoteLimitSwitchID, fwdSoftLimit, revSoftLimit, postEncoderGearing, feetPerRotation, currentLimit, enableVoltageComp, voltageCompSamples, feedbackDevice, encoderCPR, reverseSensor, perGearSettings, startingGear, startingGearNum, updaterProcessPeriodSecs, talonStatusFramesMap, controlFrameRatesMillis, slaveTalons, slaveVictors, slaveSparks);

            default:
                throw new IllegalArgumentException("Bad motor type: " + type);
        }
    }

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

    enum Type {
        SPARKMAX, TALON;
    }
}
