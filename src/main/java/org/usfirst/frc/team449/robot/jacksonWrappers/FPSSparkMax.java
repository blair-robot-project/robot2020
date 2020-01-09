package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.revrobotics.*;
import io.github.oblarg.oblog.Loggable;
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
public class FPSSparkMax implements FPSSmartMotor, Loggable {

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
     * The settings currently being used by this controller.
     */
    @NotNull
    protected PerGearSettings currentGearSettings;
    /**
     * A list of all the gears this robot has and their settings.
     */
    @NotNull
    private final Map<Integer, PerGearSettings> perGearSettings;

    /**
     * Create a new SPARK MAX Controller
     *  @param deviceID The device ID.
     * @param postEncoderGearing
     * @param feetPerRotation
     */
    public FPSSparkMax(int deviceID,
                       double postEncoderGearing,
                       double feetPerRotation,
                       @Nullable List<PerGearSettings> perGearSettings,
                       @Nullable Shiftable.gear startingGear,
                       @Nullable Integer startingGearNum) {
        spark = new CANSparkMax(deviceID, CANSparkMaxLowLevel.MotorType.kBrushless);
        canEncoder = spark.getEncoder();
        pidController = spark.getPIDController();

        this.encoderCPR = canEncoder.getCountsPerRevolution();
        this.postEncoderGearing = postEncoderGearing;
        this.feetPerRotation = feetPerRotation;

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
    }

    @Override
    public int getGear() {
        return currentGearSettings.gear;
    }

    @Override
    public void setGear(int gear) {
        //todo do this
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
        return 0;
    }

    /**
     * @return Current RPM for debug purposes
     */
    @Override
    public double encoderVelocity() {
        return 0;
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
            spark.set(velocity);
        }
    }

    /**
     * Give a velocity closed loop setpoint in FPS.
     *
     * @param velocity velocity setpoint in FPS.
     */
    public void setVelocityFPS(double velocity) {
        nativeSetpoint = FPSToEncoder(velocity);
        setpoint = velocity;
        pidController.setFF(currentGearSettings.feedForwardCalculator.calculate(velocity) / 12.);
        pidController.setReference(velocity, ControlType.kVelocity);
    }


    @Override
    public void disable() {
        spark.disable();
    }
}
