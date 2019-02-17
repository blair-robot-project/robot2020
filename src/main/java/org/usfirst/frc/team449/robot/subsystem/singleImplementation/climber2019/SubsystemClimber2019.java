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

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SubsystemClimber2019 extends Subsystem implements SubsystemBinaryMotor {

    private final FPSTalon backTalon, frontTalon, driveTalon;

    private double driveTalonCrawlVelocity;

    /**
     * The pistons for the brake.
     */
    @NotNull
    private final MappedDoubleSolenoid brakeBack, brakeFront;

    /**
     * Whether or not the motor is currently on.
     */
    private boolean motorOn;

    @JsonCreator
    public SubsystemClimber2019(FPSTalon backTalon, FPSTalon frontTalon, FPSTalon driveTalon,
                                double driveTalonCrawlVelocity,
                                @JsonProperty(required = true) @NotNull MappedDoubleSolenoid brakeBack,
                                @JsonProperty(required = true) @NotNull MappedDoubleSolenoid brakeFront) {
        this.backTalon = backTalon;
        this.frontTalon = frontTalon;
        this.driveTalon = driveTalon;
        this.driveTalonCrawlVelocity = driveTalonCrawlVelocity;
        this.brakeBack = brakeBack;
        this.brakeFront = brakeFront;
        brakeBack.set(DoubleSolenoid.Value.kForward);
        brakeFront.set(DoubleSolenoid.Value.kForward);
    }

    public void profileBack(MotionState motionState) {
        brakeBack.set(DoubleSolenoid.Value.kReverse);
        backTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    public void profileFront(MotionState motionState) {
        brakeFront.set(DoubleSolenoid.Value.kReverse);
        frontTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    public void profileDrive(MotionState motionState) {
        driveTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    public void fullStopBack() {
        brakeBack.set(DoubleSolenoid.Value.kForward);
        backTalon.disable();
    }

    public void fullStopFront() {
        brakeFront.set(DoubleSolenoid.Value.kForward);
        frontTalon.disable();
    }

    public void fullStopDrive() {
        driveTalon.disable();
    }

    //Return true if done
    public boolean profileBackUntilMovement(MotionState motionState, double initPos, double tolerance) {
        brakeBack.set(DoubleSolenoid.Value.kReverse);
        if (Math.abs(initPos - backTalon.getPositionFeet()) > tolerance) {
            return true;
        }
        backTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
        return false;
    }

    //Return true if done
    public boolean profileFrontUntilMovement(MotionState motionState, double initPos, double tolerance) {
        brakeFront.set(DoubleSolenoid.Value.kReverse);
        if (Math.abs(initPos - frontTalon.getPositionFeet()) > tolerance) {
            return true;
        }
        frontTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
        return false;
    }

    public double getBackPos() {
        return backTalon.getPositionFeet();
    }

    public double getFrontPos() {
        return frontTalon.getPositionFeet();
    }

    /**
     * Turns the motor on, and sets it to a map-specified speed.
     */
    @Override
    public void turnMotorOn() {
        driveTalon.setVelocity(driveTalonCrawlVelocity);
        motorOn = true;
    }

    /**
     * Turns the motor off.
     */
    @Override
    public void turnMotorOff() {
        driveTalon.setVelocity(0);
        motorOn = false;
    }

    /**
     * @return true if the motor is on, false otherwise.
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
}
