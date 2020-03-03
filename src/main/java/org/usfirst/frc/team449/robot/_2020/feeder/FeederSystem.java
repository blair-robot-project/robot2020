package org.usfirst.frc.team449.robot._2020.feeder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.IntakeSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class FeederSystem extends IntakeSimple {

    /** Whether the default indexing system is being overridden by having moved backwards*/
    private boolean overriding = false;

    /**
     * Default constructor
     *
     * @param motor      The motor this subsystem controls.
     * @param velocities The velocity for the motor to go at for each {@link IntakeMode}, on the
     */
    @JsonCreator
    public FeederSystem(@NotNull SimpleMotor motor, @NotNull Map<IntakeMode, Double> velocities) {
        super(motor, velocities);
    }

    @Override
    public void setMode(@NotNull IntakeMode mode) {
        if(mode.equals(IntakeMode.OUT_SLOW)) {
            overriding = true;
        }
        super.setMode(mode);
    }

    public boolean isOverriding() {
        return overriding;
    }

    public void setOverriding(boolean overriding) {
        this.overriding = overriding;
    }
}
