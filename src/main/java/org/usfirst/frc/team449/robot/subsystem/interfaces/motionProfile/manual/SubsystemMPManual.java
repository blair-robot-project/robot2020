package org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.manual;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A subsystem that can have motion profiles run on it.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public interface SubsystemMPManual {

    /**
     * Run trajectory point
     *
     * @param pos the position at the trajectory point
     * @param vel the velocity at the trajectory point
     * @param accel the acceleration at the trajectory point
     */
    void runMPPoint(double pos, double vel, double accel);


    /**
     * Disable the motors.
     */
    void disable();

    /**
     * Hold the current position.
     *
     * @param pos the position to stop at
     */
    void holdPosition(double pos);

}

