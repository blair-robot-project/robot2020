package org.usfirst.frc.team449.robot.drive.unidirectional;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.FPSSmartMotor;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;


/**
 * A drive with a cluster of any number of CANTalonSRX controlled motors on each side.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DriveUnidirectionalWithGyro extends SubsystemBase implements SubsystemAHRS, DriveUnidirectional, Loggable {


    /**
     * Right master Talon
     */
    @NotNull
    protected final FPSSmartMotor rightMaster;

    /**
     * Left master Talon
     */
    @NotNull
    protected final FPSSmartMotor leftMaster;

    /**
     * The NavX gyro
     */
    @NotNull
    private final MappedAHRS ahrs;

    /**
     * Drivetrain kinematics processor for measuring individual wheel speeds
     */
    private final DifferentialDriveKinematics driveKinematics;

    /**
     * Drivetrain odometry tracker for tracking position
     */
    private final DifferentialDriveOdometry driveOdometry;

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
     * @param trackWidthMeters The width between the left and right wheels in meters
     */
    @JsonCreator
    public DriveUnidirectionalWithGyro(@NotNull @JsonProperty(required = true) FPSTalon leftMaster,
                                       @NotNull @JsonProperty(required = true) FPSTalon rightMaster,
                                       @NotNull @JsonProperty(required = true) MappedAHRS ahrs,
                                       @NotNull @JsonProperty(required = true) double trackWidthMeters) {
        super();
        //Initialize stuff
        this.rightMaster = rightMaster;
        this.leftMaster = leftMaster;
        this.ahrs = ahrs;
        this.overrideGyro = false;
        this.driveKinematics = new DifferentialDriveKinematics(trackWidthMeters);
        this.driveOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
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
     * Set voltage output raw
     *
     * @param left The voltage output for the left side of the drive from [-12, 12]
     * @param right The voltage output for the right side of the drive from [-12, 12]
     */
    public void setVoltage(double left, double right){
        leftMaster.setPercentVoltage(left / 12);
        rightMaster.setPercentVoltage(right / 12);
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
     * @return The feedforward calculator for left motors
     */
    public SimpleMotorFeedforward getLeftFeedforwardCalculator(){
        return leftMaster.getCurrentGearFeedForward();
    }

    /**
     * @return The feedforward calculator for right motors
     */
    public SimpleMotorFeedforward getRightFeedforwardCalculator(){
        return rightMaster.getCurrentGearFeedForward();
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

    /**
     * Reset odometry tracker to current robot pose
     */
    @Log
    public void resetOdometry(){
        resetPosition();;
        driveOdometry.resetPosition(getCurrentPose(), Rotation2d.fromDegrees(getHeading()));
    }

    /**
     * Update odometry tracker with current heading, and encoder readings
     */
    public void updateOdometry(){
        //need to convert to meters
        driveOdometry.update(Rotation2d.fromDegrees(getHeading()), getLeftPos() / 3.281, getRightPos() / 3.281);
    }

    /**
     * @return Current estimated pose based on odometry tracker data
     */
    public Pose2d getCurrentPose(){
        return driveOdometry.getPoseMeters();
    }

    /**
     * @return Current wheel speeds based on encoder readings for future pose correction
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds(){
        //need to convert to meters
        return new DifferentialDriveWheelSpeeds(getLeftVel() / 3.281, getRightVel() / 3.281);
    }

    /**
     * @return Kinematics processor for wheel speeds
     */
    public DifferentialDriveKinematics getDriveKinematics(){
        return driveKinematics;
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
