package org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.TwoSideMPSubsystem.manual;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.MotionProfileData;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.SubsystemMP;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.manual.SubsystemMPManual;

/**
 * An MP subsystem with two sides that therefore needs two profiles at a time.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public interface SubsystemMPManualTwoSides extends SubsystemMPManual{

    /**
     * Runs a trajectory point on left and right sides
     *
     * @param leftPos position from the left profile
     * @param leftVel velocity from the left profile
     * @param leftAccel acceleration from the left profile
     * @param rightPos position from the right profile
     * @param rightVel velocity from the right profile
     * @param rightAccel acceleration from the right profile
     */
    void runMPPoint(double leftPos, double leftVel, double leftAccel, double rightPos, double rightVel, double rightAccel);

    /**
     * Hold the current position.
     *
     * @param leftPos the position to stop the left side at
     * @param rightPos the position to stop the right side at
     */
    void holdPosition(double leftPos, double rightPos);
}
