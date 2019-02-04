package org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.*;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.SubsystemAnalogMotor;

import java.util.function.DoubleSupplier;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class AnalogPIDCommand<T extends Subsystem & SubsystemAnalogMotor> extends PIDCommand {

    @NotNull private DoubleSupplier supplier;
    private double setPoint;
    private boolean invertInput;
    private T subsystem;

    /**
     * The range in which output is turned off to prevent "dancing" around the setpoint.
     */
    private final double deadband;

    @JsonCreator
    public AnalogPIDCommand(@JsonProperty(required = true) T subsystem,
                            @JsonProperty(required = true) DoubleSupplier supplier,
                            double deadband,
                            double p,
                            double i,
                            double d,
                            double setPoint,
                            boolean invertInput){
        super(p, i, d);
        requires(subsystem);
        this.subsystem = subsystem;
        this.invertInput = invertInput;

        if (supplier == null){
            this.setPoint = setPoint;
        }else {
            this.supplier = supplier;
        }

        //Set a deadband around the setpoint, in appropriate units, within which don't move, to avoid "dancing"
        this.deadband = deadband;
    }

    /**
     * Deadband the output of the PID loop.
     *
     * @param output The output from the WPILib angular PID loop.
     * @return That output after being deadbanded with the map-given deadband.
     */
    protected double deadbandOutput(double output) {
        return Math.abs(this.getPIDController().getError()) > deadband ? output : 0;
    }

    /**
     * Returns the input for the pid loop.
     *
     * <p>It returns the input for the pid loop, so if this command was based off of a gyro, then it
     * should return the angle of the gyro.
     *
     * <p>All subclasses of {@link PIDCommand} must override this method.
     *
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.
     *
     * @return the value the pid loop should use as input
     */
    @Override
    protected double returnPIDInput() {
        double value = supplier.getAsDouble();
        if(Double.isNaN(value)){
            return setPoint;
        }
        return !invertInput ? supplier.getAsDouble() : -supplier.getAsDouble();
    }

    /**
     * Uses the value that the pid loop calculated. The calculated value is the "output" parameter.
     * This method is a good time to set motor values, maybe something along the lines of
     * <code>driveline.tankDrive(output, -output)</code>
     *
     * <p>All subclasses of {@link PIDCommand} must override this method.
     *
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.
     *
     * @param output the value the pid loop calculated
     */
    @Override
    protected void usePIDOutput(double output) {
        output = deadbandOutput(output);
        subsystem.set(output);
    }

    /**
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * Command#end() end()} will be called.
     *
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()}
     * method for time-sensitive commands.
     *
     * <p>Returning false will result in the command never ending automatically. It may still be
     * cancelled manually or interrupted by another command. Returning true will result in the
     * command executing once and finishing immediately. We recommend using {@link InstantCommand}
     * for this.
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        return false;
    }
}