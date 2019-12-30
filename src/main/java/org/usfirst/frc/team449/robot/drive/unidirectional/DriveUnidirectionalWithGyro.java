package org.usfirst.frc.team449.robot.drive.unidirectional;

import com.fasterxml.jackson.annotation.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;


/**
 * A drive with a cluster of any number of CANTalonSRX controlled motors on each side.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DriveUnidirectionalWithGyro extends Subsystem implements SubsystemAHRS, DriveUnidirectional, Loggable {


    /**
     * Right master Talon
     */
    @NotNull
    protected final FPSTalon rightMaster;

    /**
     * Left master Talon
     */
    @NotNull
    protected final FPSTalon leftMaster;

    /**
     * The NavX gyro
     */
    @NotNull
    private final MappedAHRS ahrs;

    /**
     * Whether or not to use the NavX for driving straight
     */
    private boolean overrideGyro;

    /**
     * Cached values for various sensor readings.
     */
    @Nullable
    private Double cachedLeftVel, cachedRightVel, cachedLeftPos, cachedRightPos;

    /**
     * Default constructor.
     *
     * @param leftMaster  The master talon on the left side of the drive.
     * @param rightMaster The master talon on the right side of the drive.
     * @param ahrs        The NavX gyro for calculating this drive's heading and angular velocity.
     */
    @JsonCreator
    public DriveUnidirectionalWithGyro(@NotNull @JsonProperty(required = true) FPSTalon leftMaster,
                                       @NotNull @JsonProperty(required = true) FPSTalon rightMaster,
                                       @NotNull @JsonProperty(required = true) MappedAHRS ahrs) {
        super();
        //Initialize stuff
        this.rightMaster = rightMaster;
        this.leftMaster = leftMaster;
        this.ahrs = ahrs;
        this.overrideGyro = false;
    }

    /**
     * Set the output of each side of the drive.
     *
     * @param left  The output for the left side of the drive, from [-1, 1]
     * @param right the output for the right side of the drive, from [-1, 1]
     */
    @Override
    public void setOutput(double left, double right) {
        //scale by the max speed
        leftMaster.setVelocity(left);
        rightMaster.setVelocity(right);
    }

    /**
     * Get the velocity of the left side of the drive.
     *
     * @return The signed velocity in feet per second, or null if the drive doesn't have encoders.
     */
    @Override
    @Nullable
    public Double getLeftVel() {
        return leftMaster.getVelocity();
    }

    /**
     * Get the velocity of the right side of the drive.
     *
     * @return The signed velocity in feet per second, or null if the drive doesn't have encoders.
     */
    @Override
    @Nullable
    public Double getRightVel() {
        return rightMaster.getVelocity();
    }

    /**
     * Get the position of the left side of the drive.
     *
     * @return The signed position in feet, or null if the drive doesn't have encoders.
     */
    @Nullable
    @Override
    public Double getLeftPos() {
        return leftMaster.getPositionFeet();
    }

    /**
     * Get the position of the right side of the drive.
     *
     * @return The signed position in feet, or null if the drive doesn't have encoders.
     */
    @Nullable
    @Override
    public Double getRightPos() {
        return rightMaster.getPositionFeet();
    }

    /**
     * Get the cached velocity of the left side of the drive.
     *
     * @return The signed velocity in feet per second, or null if the drive doesn't have encoders.
     */
    @Nullable
    @Override
    public Double getLeftVelCached() {
        return cachedLeftVel;
    }

    /**
     * Get the cached velocity of the right side of the drive.
     *
     * @return The signed velocity in feet per second, or null if the drive doesn't have encoders.
     */
    @Nullable
    @Override
    public Double getRightVelCached() {
        return cachedRightVel;
    }

    /**
     * Get the cached position of the left side of the drive.
     *
     * @return The signed position in feet, or null if the drive doesn't have encoders.
     */
    @Nullable
    @Override
    public Double getLeftPosCached() {
        return cachedLeftPos;
    }

    /**
     * Get the cached position of the right side of the drive.
     *
     * @return The signed position in feet, or null if the drive doesn't have encoders.
     */
    @Nullable
    @Override
    public Double getRightPosCached() {
        return cachedRightPos;
    }

    /**
     * Completely stop the robot by setting the voltage to each side to be 0.
     */
    @Override
    public void fullStop() {
        leftMaster.setPercentVoltage(0);
        rightMaster.setPercentVoltage(0);
    }

    /**
     * If this drive uses motors that can be disabled, enable them.
     */
    @Override
    public void enableMotors() {
        leftMaster.enable();
        rightMaster.enable();
    }

    /**
     * Stuff run on first enable.
     */
    @Override
    protected void initDefaultCommand() {
        //Do nothing, the default command gets set with setDefaultCommandManual
    }

    /**
     * Get the robot's heading using the AHRS
     *
     * @return robot heading, in degrees, on [-180, 180]
     */
    @Override
    public double getHeading() {
        return ahrs.getHeading();
    }

    /**
     * Set the robot's heading.
     *
     * @param heading The heading to set to, in degrees on [-180, 180].
     */
    @Override
    public void setHeading(double heading) {
        ahrs.setHeading(heading);
    }

    /**
     * Get the robot's cached heading.
     *
     * @return robot heading, in degrees, on [-180, 180].
     */
    @Override
    public double getHeadingCached() {
        return ahrs.getCachedHeading();
    }

    /**
     * Get the robot's angular velocity.
     *
     * @return Angular velocity in degrees/sec
     */
    @Override
    public double getAngularVel() {
        return ahrs.getAngularVelocity();
    }

    /**
     * Get the robot's cached angular velocity.
     *
     * @return Angular velocity in degrees/sec
     */
    @Override
    public double getAngularVelCached() {
        return ahrs.getCachedAngularVelocity();
    }

    /**
     * Get the robot's angular displacement since being turned on.
     *
     * @return Angular displacement in degrees.
     */
    @Override
    public double getAngularDisplacement() {
        return ahrs.getAngularDisplacement();
    }

    /**
     * Get the robot's cached angular displacement since being turned on.
     *
     * @return Angular displacement in degrees.
     */
    @Override
    public double getAngularDisplacementCached() {
        return ahrs.getCachedAngularDisplacement();
    }

    /**
     * Get the pitch value.
     *
     * @return The pitch, in degrees from [-180, 180]
     */
    @Override
    public double getPitch() {
        return ahrs.getPitch();
    }

    /**
     * Get the cached pitch value.
     *
     * @return The pitch, in degrees from [-180, 180]
     */
    @Override
    public double getCachedPitch() {
        return ahrs.getCachedPitch();
    }

    /**
     * @return true if the NavX is currently overriden, false otherwise.
     */
    @Override
    @Log
    public boolean getOverrideGyro() {
        return overrideGyro;
    }

    /**
     * @param override true to override the NavX, false to un-override it.
     */
    @Override
    public void setOverrideGyro(boolean override) {
        overrideGyro = override;
    }
//
//    /**
//     * Get the headers for the data this subsystem logs every loop.
//     *
//     * @return An N-length array of String labels for data, where N is the length of the Object[] returned by getData().
//     */
//    @Override
//    @NotNull
//    @Contract(pure = true)
//    public String[] getHeader() {
//        return new String[]{"override_gyro"};
//    }
//
//    /**
//     * Get the data this subsystem logs every loop.
//     *
//     * @return An N-length array of Objects, where N is the number of labels given by getHeader.
//     */
//    @Override
//    @NotNull
//    public Object[] getData() {
//        return new Object[]{getOverrideGyro()};
//    }
//
//    /**
//     * Get the name of this object.
//     *
//     * @return A string that will identify this object in the log file.
//     */
//    @Override
//    @NotNull
//    @Contract(pure = true)
//    public String getLogName() {
//        return "Drive";
//    }

    /**
     * Disable the motors.
     */
    public void disable() {
        leftMaster.disable();
        rightMaster.disable();
    }

    /**
     * Hold the current position.
     *
     * @param pos the position to stop at
     */
    public void holdPosition(double pos) {
        holdPosition(pos, pos);
    }

    /**
     * Reset the position of the drive if it has encoders.
     */
    @Override
    public void resetPosition() {
        leftMaster.resetPosition();
        rightMaster.resetPosition();
    }

    /**
     * Updates all cached values with current ones.
     */
    @Override
    public void update() {
        cachedLeftVel = getLeftVel();
        cachedLeftPos = getLeftPos();
        cachedRightVel = getRightVel();
        cachedRightPos = getRightPos();
    }


    /**
     * Hold the current position.
     *
     * @param leftPos the position to stop the left side at
     * @param rightPos the position to stop the right side at
     */
    public void holdPosition(double leftPos, double rightPos){
        leftMaster.setPositionSetpoint(leftPos);
        rightMaster.setPositionSetpoint(rightPos);
    }
}
