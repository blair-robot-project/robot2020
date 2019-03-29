package org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;

/**
 * An analogMotor that uses position instead of velocity.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class AnalogMotorRawPosition extends Subsystem implements SubsystemAnalogMotor {

    /**
     * The motor this subsystem controls.
     */
    @NotNull
    private final FPSTalon motor;

    /**
     * Default constructor.
     *
     * @param motor  The motor this subsystem controls.
     */
    @JsonCreator
    public AnalogMotorRawPosition(@NotNull @JsonProperty(required = true) FPSTalon motor) {
        this.motor = motor;
    }

    /**
     * Set the setpoint to a given position.
     *
     * @param input The setpoint to give to the motor, in feet.
     */
    @Override
    public void set(double input) {
        motor.setPositionSetpoint(input);
    }

    /**
     * Disable the motor.
     */
    @Override
    public void disable() {
        motor.disable();
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
