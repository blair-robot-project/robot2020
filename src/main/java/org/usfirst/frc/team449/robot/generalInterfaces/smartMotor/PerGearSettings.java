package org.usfirst.frc.team449.robot.generalInterfaces.smartMotor;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.jacksonWrappers.FeedForwardCalculators.MappedFeedForwardCalculator;

/**
 * An object representing the CANTalon settings that are different for each gear.
 */
public class PerGearSettings {

    /**
     * The gear number this is the settings for.
     */
    private final int gear;

    /**
     * The forwards and reverse peak output voltages.
     */
    private final double fwdPeakOutputVoltage, revPeakOutputVoltage;

    /**
     * The forwards and reverse nominal output voltages.
     */
    private final double fwdNominalOutputVoltage, revNominalOutputVoltage;

    /**
     * The ramp rate, in volts/sec. null means no ramp rate.
     */
    @Nullable
    private final Double rampRate;

    /**
     * The maximum speed of the motor in this gear, in FPS. Used for throttle scaling.
     */
    @Nullable
    private final Double maxSpeed;

    /**
     * The PID constants for the motor in this gear. Ignored if maxSpeed is null.
     */
    private final double kP, kI, kD;

    /**
     * The position PID constants for the motor in this gear.
     */
    private final double posKP, posKI, posKD;

    /**
     * WPI object for calculating feed forward constants given a max achievable velocity
     * and acceleration
     */
    private SimpleMotorFeedforward feedForwardCalculator;

    /**
     * Default constructor.
     *
     * @param gearNum                 The gear number this is the settings for. Ignored if gear isn't null.
     * @param gear                    The gear this is the settings for. Can be null.
     * @param fwdPeakOutputVoltage    The peak output voltage for closed-loop modes in the forwards direction, in
     *                                volts. Defaults to 12.
     * @param revPeakOutputVoltage    The peak output voltage for closed-loop modes in the reverse direction, in
     *                                volts. Defaults to -fwdPeakOutputVoltage.
     * @param fwdNominalOutputVoltage The minimum output voltage for closed-loop modes in the forwards direction.
     *                                This does not rescale, it just sets any output below this voltage to this
     *                                voltage. Defaults to 0.
     * @param revNominalOutputVoltage The minimum output voltage for closed-loop modes in the reverse direction.
     *                                This does not rescale, it just sets any output below this voltage to this
     *                                voltage. Defaults to -fwdNominalOutputVoltage.
     *
     * @param feedForwardCalculator   The component for calculating feedforwards in closed-loop control modes.
     *
     * @param rampRate                The ramp rate, in volts/sec. Can be null, and if it is, no ramp rate is used.
     * @param maxSpeed                The maximum speed of the motor in this gear, in FPS. Used for throttle
     *                                scaling. Ignored if kVFwd is null. Calculated from the drive characterization
     *                                terms if null.
     * @param kP                      The proportional PID constant for the motor in this gear. Ignored if kVFwd is
     *                                null. Defaults to 0.
     * @param kI                      The integral PID constant for the motor in this gear. Ignored if kVFwd is
     *                                null. Defaults to 0.
     * @param kD                      The derivative PID constant for the motor in this gear. Ignored if kVFwd is
     *                                null. Defaults to 0.
     * @param posKP                   The proportional PID constant for position control on the motor in this gear. Ignored if kVFwd is
     *                                null. Defaults to 0.
     * @param posKI                   The integral PID constant for position control on the motor in this gear. Ignored if kVFwd is
     *                                null. Defaults to 0.
     * @param posKD                   The derivative PID constant for position control on the motor in this gear. Ignored if kVFwd is
     *                                null. Defaults to 0.
     */
    @JsonCreator
    public PerGearSettings(int gearNum,
                           @Nullable Shiftable.gear gear,
                           @Nullable Double fwdPeakOutputVoltage,
                           @Nullable Double revPeakOutputVoltage,
                           @Nullable Double fwdNominalOutputVoltage,
                           @Nullable Double revNominalOutputVoltage,
                           @Nullable MappedFeedForwardCalculator feedForwardCalculator,
                           @Nullable Double rampRate,
                           @Nullable Double maxSpeed,
                           double kP,
                           double kI,
                           double kD,
                           double posKP,
                           double posKI,
                           double posKD) {
        this.gear = gear != null ? gear.getNumVal() : gearNum;
        this.fwdPeakOutputVoltage = fwdPeakOutputVoltage != null ? fwdPeakOutputVoltage : 12;
        this.revPeakOutputVoltage = revPeakOutputVoltage != null ? revPeakOutputVoltage :
                -this.fwdPeakOutputVoltage;
        this.fwdNominalOutputVoltage = fwdNominalOutputVoltage != null ? fwdNominalOutputVoltage : 0;
        this.revNominalOutputVoltage = revNominalOutputVoltage != null ? revNominalOutputVoltage :
                -this.fwdNominalOutputVoltage;
        this.feedForwardCalculator = feedForwardCalculator != null ? feedForwardCalculator :
                new SimpleMotorFeedforward(0,0);
        this.rampRate = rampRate;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.posKP = posKP;
        this.posKI = posKI;
        this.posKD = posKD;
        this.maxSpeed = maxSpeed;
    }

    /**
     * Empty constructor that uses all default options.
     */
    public PerGearSettings() {
        this(0, null, null, null, null, null, null, null, null, 0, 0, 0, 0, 0 ,0);
    }

    /**
     * @return The gear number this is the settings for.
     */
    public int getGear() {
        return gear;
    }

    /**
     * @return The peak output voltage for closed-loop modes in the forwards direction, in volts.
     */
    public double getFwdPeakOutputVoltage() {
        return fwdPeakOutputVoltage;
    }

    /**
     * @return The peak output voltage for closed-loop modes in the reverse direction, in volts.
     */
    public double getRevPeakOutputVoltage() {
        return revPeakOutputVoltage;
    }

    /**
     * @return The minimum output voltage for closed-loop modes in the forwards direction. This does not rescale, it
     * just sets any output below this voltage to this voltage.
     */
    public double getFwdNominalOutputVoltage() {
        return fwdNominalOutputVoltage;
    }

    /**
     * @return The minimum output voltage for closed-loop modes in the reverse direction. This does not rescale, it
     * just sets any output below this voltage to this voltage.
     */
    public double getRevNominalOutputVoltage() {
        return revNominalOutputVoltage;
    }

    /**
     * @return Feedforward calculator for this gear
     */
    public SimpleMotorFeedforward getFeedForwardCalculator(){
        return feedForwardCalculator;
    }

    /**
     * @return The ramp rate, in volts/sec.
     */
    @Nullable
    public Double getRampRate() {
        return rampRate;
    }

    /**
     * @return The maximum speed of the motor in this gear, in FPS.
     */
    @Nullable
    public Double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @return The proportional PID constant for the motor in this gear.
     */
    public double getkP() {
        return kP;
    }

    /**
     * @return The integral PID constant for the motor in this gear.
     */
    public double getkI() {
        return kI;
    }

    /**
     * @return The derivative PID constant for the motor in this gear.
     */
    public double getkD() {
        return kD;
    }

    public double getPosKP() {
        return posKP;
    }

    public double getPosKI() {
        return posKI;
    }

    public double getPosKD() {
        return posKD;
    }

}
