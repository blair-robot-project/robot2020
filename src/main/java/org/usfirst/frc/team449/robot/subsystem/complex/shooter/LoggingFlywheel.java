package org.usfirst.frc.team449.robot.subsystem.complex.shooter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.conditional.SubsystemConditional;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.SubsystemFlywheel;

/**
 * A flywheel multiSubsystem with a single flywheel and a single-motor feeder system.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LoggingFlywheel extends SubsystemBase implements SubsystemFlywheel, SubsystemConditional, io.github.oblarg.oblog.Loggable {

    /**
     * The flywheel's Talon
     */
    @NotNull
    private final FPSSmartMotor shooterMotor;

    /**
     * The feeder's motor
     */
    @NotNull
    private final SimpleMotor kickerMotor;

    /**
     * How fast to run the feeder, from [-1, 1]
     */
    private final double feederThrottle;

    /**
     * Throttle at which to run the multiSubsystem, from [-1, 1]
     */
    private final double shooterThrottle;

    /**
     * Time from giving the multiSubsystem voltage to being ready to fire, in seconds.
     */
    private final double spinUpTimeoutSecs;

    @Nullable
    private final Double minShootingSpeedFPS;

    /**
     * Whether the flywheel is currently commanded to spin
     */
    @NotNull
    private SubsystemFlywheel.FlywheelState state;

    /**
     * Whether the condition was met last time caching was done.
     */
    private boolean conditionMetCached;

    /**
     * Default constructor
     *
     * @param shooterMotor        The motor controlling the flywheel.
     * @param shooterThrottle     The throttle, from [-1, 1], at which to run the multiSubsystem.
     * @param kickerMotor         The motor controlling the feeder.
     * @param feederThrottle      The throttle, from [-1, 1], at which to run the feeder.
     * @param spinUpTimeoutSecs   The amount of time, in seconds, it takes for the multiSubsystem to get up to speed.
     *                            Defaults to {@literal 0}.
     * @param minShootingSpeedFPS The speed, in feet per second, at which the flywheel is ready to begin shooting.
     *                            Defaults to {@literal null}, meaning that there is no minimum speed requirement.
     */
    @JsonCreator
    public LoggingFlywheel(@NotNull @JsonProperty(required = true) FPSSmartMotor shooterMotor,
                           @JsonProperty(required = true) double shooterThrottle,
                           @NotNull @JsonProperty(required = true) SimpleMotor kickerMotor,
                           @JsonProperty(required = true) double feederThrottle,
                           double spinUpTimeoutSecs,
                           @Nullable Double minShootingSpeedFPS) {
        this.shooterMotor = shooterMotor;
        this.shooterThrottle = shooterThrottle;
        this.kickerMotor = kickerMotor;
        this.feederThrottle = feederThrottle;
        this.spinUpTimeoutSecs = spinUpTimeoutSecs;
        this.minShootingSpeedFPS = minShootingSpeedFPS;

        this.state = FlywheelState.OFF;
    }

//    /**
//     * Get the headers for the data this subsystem logs every loop.
//     *
//     * @return An N-length array of String labels for data, where N is the length of the Object[] returned by getData().
//     */
//    @NotNull
//    @Override
//    public String[] getHeader() {
//        return new String[]{"speed",
//                "setpoint",
//                "error",
//                "voltage",
//                "current"};
//    }
//
//    /**
//     * Get the data this subsystem logs every loop.
//     *
//     * @return An N-length array of Objects, where N is the number of labels given by getHeader.
//     */
//    @NotNull
//    @Override
//    public Object[] getData() {s
//        return new Object[]{shooterMotor.getVelocity(),
//                shooterMotor.getSetpoint(),
//                shooterMotor.getError(),
//                shooterMotor.getOutputVoltage(),
//                shooterMotor.getOutputCurrent()};
//    }
//
//    /**
//     * Get the name of this object.
//     *
//     * @return A string that will identify this object in the log file.
//     */
//    @NotNull
//    @Override
//    public String getLogName() {
//        return "loggingShooter";
//    }

    /**
     * Turn the multiSubsystem on to a map-specified speed.
     */
    @Override
    public void turnFlywheelOn() {
        shooterMotor.enable();
        shooterMotor.setVelocity(shooterThrottle);
    }

    /**
     * Turn the multiSubsystem off.
     */
    @Override
    public void turnFlywheelOff() {
        shooterMotor.disable();
    }

    /**
     * Start feeding balls into the multiSubsystem.
     */
    @Override
    public void turnFeederOn() {
        kickerMotor.enable();
        kickerMotor.setVelocity(feederThrottle);
    }

    /**
     * Stop feeding balls into the multiSubsystem.
     */
    @Override
    public void turnFeederOff() {
        kickerMotor.disable();
    }

    /**
     * @return The current state of the multiSubsystem.
     */
    @NotNull
    @Override
    public SubsystemFlywheel.FlywheelState getFlywheelState() {
        return state;
    }

    /**
     * @param state The state to switch the multiSubsystem to.
     */
    @Override
    public void setFlywheelState(@NotNull SubsystemFlywheel.FlywheelState state) {
        this.state = state;
    }

    @Log
    public String getFlywheelStateLogged() {
        return state.name();
    }

    /**
     * @return Expected time from giving the multiSubsystem voltage to being ready to fire, in seconds.
     */
    @Override
    @Log
    public double getSpinUpTimeoutSecs() {
        return spinUpTimeoutSecs;
    }

    @Override
    @Log
    public boolean isAtShootingSpeed() {
        if (this.minShootingSpeedFPS == null) return false;
        Double actualVelocity = this.shooterMotor.getVelocity();
        return !Double.isNaN(actualVelocity) && actualVelocity > this.minShootingSpeedFPS;
    }

    @Log
    public double actualSpeed() {
        return this.shooterMotor.getVelocity();
    }

    /**
     * @return true if the condition is met, false otherwise
     */
    @Override
    public boolean isConditionTrue() {
        return this.isAtShootingSpeed();
    }

    /**
     * @return true if the condition was met when cached, false otherwise
     */
    @Override
    @Log
    public boolean isConditionTrueCached() {
        return conditionMetCached;
    }

    /**
     * Updates all cached values with current ones.
     */
    @Override
    public void update() {
        conditionMetCached = isConditionTrue();
    }
}