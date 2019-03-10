package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team254.lib.util.motion.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDoubleSolenoid;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.SubsystemBinaryMotor;

/**
 * A 2019 climbing subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SubsystemClimber2019 extends Subsystem implements SubsystemBinaryMotor {

    /**
     * Talons for the back elevator, front elevator, and leg-drive.
     */
    @NotNull
    private final FPSTalon backTalon, frontTalon, driveTalon;

    /**
     * Velocity, in feet/second at which the drive and leg-drive should crawl until the IR trips.
     * Gets overridden in Climb command.
     */
    private double crawlVelocity;

    /**
     * The pistons for the brake.
     */
    @NotNull
    private final MappedDoubleSolenoid brakeBack, brakeFront;

    /**
     * Whether or not the leg-drive motor is currently on.
     */
    private boolean motorOn;

    /**
     * Default constructor
     *
     * @param backTalon     The Talon for the back elevator.
     * @param frontTalon    The Talon for the front elevator.
     * @param driveTalon    The Talon for the leg-drive.
     * @param crawlVelocity Velocity, in feet/second at which the drive and leg-drive should crawl until the IR trips.
     *                      Gets overridden in Climb command.
     * @param brakeBack     The piston to brake the back elevator.
     * @param brakeFront    The piston to brake the front elevator.
     */
    @JsonCreator
    public SubsystemClimber2019(@JsonProperty(required = true) @NotNull FPSTalon backTalon,
                                @JsonProperty(required = true) @NotNull FPSTalon frontTalon,
                                @JsonProperty(required = true) @NotNull FPSTalon driveTalon,
                                double crawlVelocity,
                                @JsonProperty(required = true) @NotNull MappedDoubleSolenoid brakeBack,
                                @JsonProperty(required = true) @NotNull MappedDoubleSolenoid brakeFront) {
        this.backTalon = backTalon;
        this.frontTalon = frontTalon;
        this.driveTalon = driveTalon;
        this.crawlVelocity = crawlVelocity;
        this.brakeBack = brakeBack;
        this.brakeFront = brakeFront;
        brakeBack.set(DoubleSolenoid.Value.kForward);
        brakeFront.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Give the back elevator a motion state to reach.
     * @param motionState The desired motion state.
     */
    public void profileBack(MotionState motionState) {
        brakeBack.set(DoubleSolenoid.Value.kReverse);
        backTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    /**
     * Give the front elevator a motion state to reach.
     * @param motionState The desired motion state.
     */
    public void profileFront(MotionState motionState) {
        brakeFront.set(DoubleSolenoid.Value.kReverse);
        frontTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    /**
     * Give the back elevator a motion state to reach, but offset the desired position by a given value.
     * @param motionState The desired motion state.
     * @param offset      The position offset, in feet.
     */
    public void profileBackWithOffset(MotionState motionState, double offset) {
        backTalon.executeMPPoint(motionState.pos() + offset, motionState.vel(), motionState.acc());
    }

    /**
     * Give the back elevator a motion state to reach, but offset the desired position by a given value.
     * @param motionState The desired motion state.
     * @param offset      The position offset, in feet.
     */
    public void profileFrontWithOffset(MotionState motionState, double offset) {
        frontTalon.executeMPPoint(motionState.pos() + offset, motionState.vel(), motionState.acc());
    }

    /**
     * Give the leg-drive a motion state to reach.
     * @param motionState The desired motion state.
     */
    public void profileDrive(MotionState motionState) {
        driveTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    /**
     * Give the leg-drive a motion state to reach, but offset the desired position by a given value.
     * @param motionState The desired motion state.
     * @param offset      The position offset, in feet.
     */
    public void profileDriveWithOffset(MotionState motionState, double offset) {
        driveTalon.executeMPPoint(motionState.pos() + offset, motionState.vel(), motionState.acc());
    }

    /**
     * Stop the back elevator.
     */
    public void fullStopBack() {
        brakeBack.set(DoubleSolenoid.Value.kForward);
        backTalon.disable();
    }

    /**
     * Stop the front elevator.
     */
    public void fullStopFront() {
        brakeFront.set(DoubleSolenoid.Value.kForward);
        frontTalon.disable();
    }

    /**
     * Stop the leg-drive.
     */
    public void fullStopDrive() {
        driveTalon.disable();
    }

    /**
     * Try to reach a motion state on the back elevator as long as it hasn't moved a given tolerance from its initial
     * position. Used to unstick brake disengagement.
     * @param motionState The motion state to try to reach.
     * @param initPos     The initial position of the back elevator.
     * @param tolerance   The distance required to move, in feet.
     * @return True if we've made it past the desired tolerance, false otherwise.
     */
    public boolean profileBackUntilMovement(MotionState motionState, double initPos, double tolerance) {
        brakeBack.set(DoubleSolenoid.Value.kReverse);
        if (Math.abs(initPos - backTalon.getPositionFeet()) > tolerance) {
            return true;
        }
        backTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
        return false;
    }

    /**
     * Try to reach a motion state on the front elevator as long as it hasn't moved a given tolerance from its initial
     * position. Used to unstick brake disengagement.
     * @param motionState The motion state to try to reach.
     * @param initPos     The initial position of the front elevator.
     * @param tolerance   The distance required to move, in feet.
     * @return true if we've made it past the desired tolerance, false otherwise.
     */
    public boolean profileFrontUntilMovement(MotionState motionState, double initPos, double tolerance) {
        brakeFront.set(DoubleSolenoid.Value.kReverse);
        if (Math.abs(initPos - frontTalon.getPositionFeet()) > tolerance) {
            return true;
        }
        frontTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
        return false;
    }

    /**
     * @return the position of the back motor.
     */
    public double getBackPos() {
        return backTalon.getPositionFeet();
    }

    /**
     * @return the position of the front motor.
     */
    public double getFrontPos() {
        return frontTalon.getPositionFeet();
    }

    /**
     * @return the position of the leg-drive motor.
     */
    public double getDrivePos() {
        return driveTalon.getPositionFeet();
    }

    /**
     * Set the velocity at which the leg-drive crawls until the IR trips.
     * @param vel The crawl velocity to set.
     */
    public void setCrawlVelocity(double vel) {
        crawlVelocity = vel;
    }

    /**
     * Turns the leg-drive motor on, and sets it to its designated speed.
     */
    @Override
    public void turnMotorOn() {
        driveTalon.enable();
        driveTalon.setVelocity(crawlVelocity);
        motorOn = true;
    }

    /**
     * Turns the leg-drive motor off.
     */
    @Override
    public void turnMotorOff() {
//        driveTalon.setVelocity(0);
//        driveTalon.disable();
//        motorOn = false;
    }

    /**
     * @return true if the leg-drive motor is on, false otherwise.
     */
    @Override
    public boolean isMotorOn() {
        return motorOn;
    }

    /**
     * Initialize the default command for a subsystem By default subsystems have no default command, but if they do, the
     * default command is set with this method. It is called on all Subsystems by CommandBase in the users program after
     * all the Subsystems are created.
     */
    @Override
    protected void initDefaultCommand() {
        //Do nothing!
    }

    public void setFrontVoltage(double voltage) {
        frontTalon.setPercentVoltage(voltage/12.);
    }

    public void setBackVoltage(double voltage) {
        backTalon.setPercentVoltage(voltage/12.);
    }
}
