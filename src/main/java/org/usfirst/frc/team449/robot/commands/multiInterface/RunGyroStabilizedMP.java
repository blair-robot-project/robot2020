package org.usfirst.frc.team449.robot.commands.multiInterface;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.BufferTimer;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.other.MotionProfileData;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.commands.PIDAngleCommand;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.TwoSideMPSubsystem.manual.SubsystemMPManualTwoSides;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class RunGyroStabilizedMP<T extends Subsystem & SubsystemMPManualTwoSides & SubsystemAHRS> extends PIDAngleCommand {

    /**
     * The output of the PID loop. Field to avoid garbage collection.
     */
    private double output;

    /**
     * Amount of time this command is allowed to run for, in milliseconds
     */
    private final long timeout;

    /**
     * The subsystem to run this command on
     */
    protected final T subsystem;

    /**
     * The motion profile points that the command will execute (left side)
     */
    private final MotionProfileData left;

    /**
     * The motion profile points that the command will execute (right side)
     */
    private final MotionProfileData right;

    private final double startAngle;

    /**
     * The time this command started running at.
     */
    private long startTime;

    /**
     *
     */
    private int index;

    /**
     * Default constructor.
     *
     * @param absoluteTolerance The maximum number of degrees off from the target at which we can be considered within
     *                          tolerance.
     * @param onTargetBuffer    A buffer timer for having the loop be on target before it stops running. Can be null for no
     *                          buffer.
     * @param minimumOutput     The minimum output of the loop. Defaults to zero.
     * @param maximumOutput     The maximum output of the loop. Can be null, and if it is, no maximum output is used.
     * @param loopTimeMillis    The time, in milliseconds, between each loop iteration. Defaults to 20 ms.
     * @param deadband          The deadband around the setpoint, in degrees, within which no output is given to the motors.
     *                          Defaults to zero.
     * @param inverted          Whether the loop is inverted. Defaults to false.
     * @param subsystem         The subsystem to execute this command on.
     * @param kP                Proportional gain. Defaults to zero.
     * @param kI                Integral gain. Defaults to zero.
     * @param kD                Derivative gain. Defaults to zero.
     * @param timeout   the time that this command will run for, in seconds
     * @param startAngle The angle that the profile starts at. Defaults to 0.
     */
    @JsonCreator
    public RunGyroStabilizedMP(double absoluteTolerance,
                               @Nullable BufferTimer onTargetBuffer,
                               double minimumOutput,
                               @Nullable Double maximumOutput,
                               @Nullable Integer loopTimeMillis,
                               double deadband, boolean inverted,
                               @NotNull T subsystem,
                               double kP,
                               double kI,
                               double kD,
                               @JsonProperty(required = true) double timeout,
                               @JsonProperty(required = true) MotionProfileData left,
                               @JsonProperty(required = true) MotionProfileData right,
                               double startAngle) {
        super(absoluteTolerance, onTargetBuffer, minimumOutput, maximumOutput, loopTimeMillis, deadband, inverted, subsystem, kP, kI, kD);

        this.subsystem = subsystem;
        this.timeout = (long) (timeout * 1000.);
        this.left = left;
        this.right = right;
        this.startAngle = startAngle;
    }

    /**
     * Returns whether this command is finished. If it is, then the command will be removed and {@link Command#end()
     * end()} will be called.
     *
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()}
     * method for time-sensitive commands.
     *
     * <p>Returning false will result in the command never ending automatically. It may still be
     * cancelled manually or interrupted by another command. Returning true will result in the command executing once
     * and finishing immediately. We recommend using {@link InstantCommand} for this.
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    protected boolean isFinished() {
        return (index == left.getData().length - 1 || timeout <= Clock.currentTimeMillis() - startTime);
    }

    @Override
    protected void initialize() {
        //Record the start time.
        startTime = Clock.currentTimeMillis();
        this.getPIDController().enable();
        Shuffleboard.addEventMarker("RunProfile init", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("RunProfile init");
    }

    @Override
    protected void execute() {
        if (left.getPointTimeMillis() != 0) {
            index = Math.min((int) (Clock.currentTimeMillis() - startTime) / left.getPointTimeMillis(), left.getData().length - 1);
        }
        double[] profileDataLeft = left.getData()[index];
        double[] profileDataRight = right.getData()[index];
        this.getPIDController().setSetpoint(clipTo180(profileDataLeft[3] + startAngle));
        output = processPIDOutput(this.getPIDController().get());
        subsystem.runMPPoint(profileDataLeft[0], profileDataLeft[1] - output, profileDataLeft[2],
                            profileDataRight[0], profileDataRight[1] + output, profileDataRight[2]);
    }

    @Override
    protected void end() {
        double leftPos = left.getData()[left.getData().length - 1][0];
        double rightPos = right.getData()[right.getData().length - 1][0];
        subsystem.holdPosition(leftPos, rightPos);
        this.getPIDController().disable();
        Shuffleboard.addEventMarker("RunProfile end.", this.getClass().getSimpleName(), EventImportance.kNormal);
        //Logger.addEvent("RunProfile end.", this.getClass());
    }

}
