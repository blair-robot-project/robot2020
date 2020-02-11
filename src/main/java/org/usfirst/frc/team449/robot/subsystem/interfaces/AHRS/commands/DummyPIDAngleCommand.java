package org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.BufferTimer;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;

public class DummyPIDAngleCommand extends PIDAngleCommand {
    /**
     * Default constructor.
     *
     * @param absoluteTolerance The maximum number of degrees off from the target at which we can be considered within
     *                          tolerance.
     * @param onTargetBuffer    A buffer timer for having the loop be on target before it stops running. Can be null for
     *                          no buffer.
     * @param minimumOutput     The minimum output of the loop. Defaults to zero.
     * @param maximumOutput     The maximum output of the loop. Can be null, and if it is, no maximum output is used.
     * @param loopTimeMillis    The time, in milliseconds, between each loop iteration. Defaults to 20 ms.
     * @param deadband          The deadband around the setpoint, in degrees, within which no output is given to the
     *                          motors. Defaults to zero.
     * @param inverted          Whether the loop is inverted. Defaults to false.
     * @param subsystem         The subsystem to execute this command on.
     * @param kP                Proportional gain. Defaults to zero.
     * @param kI                Integral gain. Defaults to zero.
     * @param kD                Derivative gain. Defaults to zero.
     */
    public DummyPIDAngleCommand(double absoluteTolerance, @Nullable BufferTimer onTargetBuffer, double minimumOutput, @Nullable Double maximumOutput, @Nullable Integer loopTimeMillis, double deadband, boolean inverted, @NotNull SubsystemAHRS subsystem, double kP, double kI, double kD) {
        super(absoluteTolerance, onTargetBuffer, minimumOutput, maximumOutput, loopTimeMillis, deadband, inverted, subsystem, kP, kI, kD);
    }
}
