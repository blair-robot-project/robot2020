package org.usfirst.frc.team449.robot.subsystem.interfaces.climber;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public interface SubsystemClimberFoldingArm {
    void raise();

    void lower();

    void off();
}
