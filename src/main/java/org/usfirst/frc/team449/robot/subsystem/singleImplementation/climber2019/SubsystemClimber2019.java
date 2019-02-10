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

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SubsystemClimber2019 extends Subsystem {

    private final FPSTalon backTalon, frontTalon;

    /**
     * The pistons for the brake.
     */
    @NotNull
    private final MappedDoubleSolenoid brakeBack, brakeFront;

    @JsonCreator
    public SubsystemClimber2019(FPSTalon backTalon, FPSTalon frontTalon,
                                @JsonProperty(required = true) @NotNull MappedDoubleSolenoid brakeBack,
                                @JsonProperty(required = true) @NotNull MappedDoubleSolenoid brakeFront) {
        this.backTalon = backTalon;
        this.frontTalon = frontTalon;
        this.brakeBack = brakeBack;
        this.brakeFront = brakeFront;
        brakeBack.set(DoubleSolenoid.Value.kForward);
        brakeFront.set(DoubleSolenoid.Value.kReverse);
    }

    public void profileBack(MotionState motionState) {
        brakeBack.set(DoubleSolenoid.Value.kReverse);
        backTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    public void profileFront(MotionState motionState) {
        brakeFront.set(DoubleSolenoid.Value.kReverse);
        frontTalon.executeMPPoint(motionState.pos(), motionState.vel(), motionState.acc());
    }

    public void fullStopBack() {
        brakeBack.set(DoubleSolenoid.Value.kForward);
        backTalon.disable();
    }

    public void fullStopFront() {
        brakeFront.set(DoubleSolenoid.Value.kForward);
        frontTalon.disable();
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
