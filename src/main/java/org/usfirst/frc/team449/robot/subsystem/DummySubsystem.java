package org.usfirst.frc.team449.robot.subsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.DummyAHRS;

public class DummySubsystem implements Subsystem {

    @JsonCreator
    public DummySubsystem() {

    }

}
