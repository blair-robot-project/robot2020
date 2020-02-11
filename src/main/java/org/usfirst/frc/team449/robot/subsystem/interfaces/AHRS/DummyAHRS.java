package org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedAHRS;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DummyAHRS extends SubsystemBase implements SubsystemAHRS, Loggable {
    /**
     * The NavX gyro
     */
    @NotNull
    private final MappedAHRS ahrs;

    private boolean overrideGyro;

    @JsonCreator
    public DummyAHRS(@NotNull @JsonProperty(required = true) MappedAHRS ahrs){
        this.ahrs = ahrs;
    }

    /**
     * Get the robot's heading using the AHRS
     *
     * @return robot heading, in degrees, on [-180, 180]
     */
    @Override
    @Log
    public double getHeading() {
        return ahrs.getHeading();
    }

    /**
     * Set the robot's heading.
     *
     * @param heading The heading to set to, in degrees on [-180, 180].
     */
    @Override
    public void setHeading(double heading) {
        ahrs.setHeading(heading);
    }

    /**
     * Get the robot's cached heading.
     *
     * @return robot heading, in degrees, on [-180, 180].
     */
    @Override
    public double getHeadingCached() {
        return ahrs.getCachedHeading();
    }

    /**
     * Get the robot's angular velocity.
     *
     * @return Angular velocity in degrees/sec
     */
    @Override
    public double getAngularVel() {
        return ahrs.getAngularVelocity();
    }

    /**
     * Get the robot's cached angular velocity.
     *
     * @return Angular velocity in degrees/sec
     */
    @Override
    public double getAngularVelCached() {
        return ahrs.getCachedAngularVelocity();
    }

    /**
     * Get the robot's angular displacement since being turned on.
     *
     * @return Angular displacement in degrees.
     */
    @Override
    public double getAngularDisplacement() {
        return ahrs.getAngularDisplacement();
    }

    /**
     * Get the robot's cached angular displacement since being turned on.
     *
     * @return Angular displacement in degrees.
     */
    @Override
    public double getAngularDisplacementCached() {
        return ahrs.getCachedAngularDisplacement();
    }

    /**
     * Get the pitch value.
     *
     * @return The pitch, in degrees from [-180, 180]
     */
    @Override
    public double getPitch() {
        return ahrs.getPitch();
    }

    /**
     * Get the cached pitch value.
     *
     * @return The pitch, in degrees from [-180, 180]
     */
    @Override
    public double getCachedPitch() {
        return ahrs.getCachedPitch();
    }

    /**
     * @return true if the NavX is currently overriden, false otherwise.
     */
    @Override
    @Log
    public boolean getOverrideGyro() {
        return overrideGyro;
    }

    /**
     * @param override true to override the NavX, false to un-override it.
     */
    @Override
    public void setOverrideGyro(boolean override) {
        overrideGyro = override;
    }
}
