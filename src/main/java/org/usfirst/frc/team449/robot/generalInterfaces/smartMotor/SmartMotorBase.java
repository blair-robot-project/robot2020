package org.usfirst.frc.team449.robot.generalInterfaces.smartMotor;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;

import java.util.*;

import static java.util.Objects.requireNonNullElseGet;
import static org.usfirst.frc.team449.robot.util.Util.defaultIfNull;

/**
 * Abstract base class for motion-profile supporting motor controllers such as {@link org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon}.
 */
public abstract class SmartMotorBase implements SmartMotor {
    protected static final double VOLTAGE_COMPENSATION = 12;
    /**
     * The PDP this motor is connected to.
     */
    @Nullable
    protected final org.usfirst.frc.team449.robot.jacksonWrappers.PDP PDP;
    /**
     * The counts per rotation of the encoder being used, or null if there is no encoder.
     */
    @Nullable
    protected final Integer encoderCPR;
    /**
     * The coefficient the output changes by after being measured by the encoder, e.g. this would be 1/70 if there was a
     * 70:1 gearing between the encoder and the final output.
     */
    protected final double postEncoderGearing;
    /**
     * The number of feet travelled per rotation of the motor this is attached to, or null if there is no encoder.
     */
    protected final double feetPerRotation;
    /**
     * The period for this controller's motion updater, in seconds.
     */
    protected final double updaterProcessPeriodSecs;
    /**
     * A list of all the gears this robot has and their settings.
     */
    @NotNull
    protected final Map<Integer, PerGearSettings> perGearSettings;
    /**
     * The motor's name, used for logging purposes.
     */
    @NotNull
    protected final String name;
    /**
     * The component for doing linear regression to find the resistance.
     */
    @Nullable
    protected final RunningLinRegComponent voltagePerCurrentLinReg;
    /**
     * Whether the forwards or reverse limit switches are normally open or closed, respectively.
     */
    protected final boolean fwdLimitSwitchNormallyOpen;
    protected final boolean revLimitSwitchNormallyOpen;
    /**
     * The minimum number of points that must be in the bottom-level MP buffer before starting a profile.
     */
    protected final int minNumPointsInBottomBuffer;
    /**
     * The settings currently being used by this Talon.
     */
    @NotNull
    protected PerGearSettings currentGearSettings;
    /**
     * The setpoint in native units. Field to avoid garbage collection.
     */
    protected double nativeSetpoint;
    /**
     * The time at which the motion profile status was last checked. Only getting the status once per tic avoids CAN
     * traffic.
     */
    protected long timeMPStatusLastRead;
    /**
     * RPS as used in a unit conversion method. Field to avoid garbage collection.
     */
    protected Double RPS;

    public SmartMotorBase(@JsonProperty(required = true) final int port,
                          @Nullable final org.usfirst.frc.team449.robot.jacksonWrappers.PDP PDP,
                          @Nullable final Integer encoderCPR,
                          final double postEncoderGearing,
                          final double feetPerRotation,
                          final double updaterProcessPeriodSecs,
                          @Nullable final String name,
                          @Nullable final RunningLinRegComponent voltagePerCurrentLinReg,
                          final boolean fwdLimitSwitchNormallyOpen,
                          final boolean revLimitSwitchNormallyOpen,
                          @Nullable final Integer minNumPointsInBottomBuffer,
                          @Nullable List<PerGearSettings> perGearSettings,
                          @Nullable final Shiftable.gear startingGear,
                          @Nullable final Integer startingGearNum) {
        this.PDP = PDP;
        this.encoderCPR = encoderCPR;
        this.postEncoderGearing = postEncoderGearing;
        this.feetPerRotation = feetPerRotation;
        this.updaterProcessPeriodSecs = updaterProcessPeriodSecs;
        this.fwdLimitSwitchNormallyOpen = fwdLimitSwitchNormallyOpen;
        this.revLimitSwitchNormallyOpen = revLimitSwitchNormallyOpen;
        this.name = defaultIfNull(name, this.getClass().getName() + "_" + port);
        this.voltagePerCurrentLinReg = voltagePerCurrentLinReg;
        this.minNumPointsInBottomBuffer = defaultIfNull(minNumPointsInBottomBuffer, 20);

        this.perGearSettings = new HashMap<>();
        //If given no gear settings, use the default values.
        if (perGearSettings == null || perGearSettings.size() == 0) {
            this.perGearSettings.put(0, new PerGearSettings());
        }
        //Otherwise, map the settings to the gear they are.
        else {
            for (final PerGearSettings settings : perGearSettings) {
                this.perGearSettings.put(settings.getGear(), settings);
            }
        }

        int currentGear = startingGear == null
                ? requireNonNullElseGet(startingGearNum, () -> Collections.min(this.perGearSettings.keySet()))
                : startingGear.getNumVal();

        this.setGear(currentGear);
    }

    /**
     * @return The gear this subsystem is currently in.
     */
    @Override
    @Contract(pure = true)
    public int getGear() {
        return this.currentGearSettings.getGear();
    }
}
