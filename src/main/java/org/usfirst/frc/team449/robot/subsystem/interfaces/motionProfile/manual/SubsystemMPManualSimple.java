package org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.manual;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;

/**
 * A simple subsystem that uses a Talon for motion profiling.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SubsystemMPManualSimple extends Subsystem implements SubsystemMPManual, Loggable {

    /**
     * The motor this subsystem controls.
     */
    private final FPSTalon motor;

    /**
     * Default constructor.
     *
     * @param motor The motor this subsystem controls.
     */
    @JsonCreator
    public SubsystemMPManualSimple(@NotNull @JsonProperty(required = true) FPSTalon motor) {
        this.motor = motor;
    }

    /**
     * Disable the motors.
     */
    @Override
    public void disable() {
        motor.disable();
    }

    /**
     * Hold the current position.
     */
    @Override
    public void holdPosition(double pos) {
        motor.executeMPPoint(pos,0,0);
    }

    /**
     * Run the trajectory point
     */
    @Override
    public void runMPPoint(double pos, double vel, double accel){
        motor.executeMPPoint(pos,vel,accel);
    }

    /**
     * Initialize the default command for a subsystem By default subsystems have no default command, but if they do, the
     * default command is set with this method. It is called on all Subsystems by CommandBase in the users program after
     * all the Subsystems are created.
     */
    @Override
    protected void initDefaultCommand() {
        //Do nothing
    }
}
