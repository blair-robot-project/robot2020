package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

/**
 * Finds the distance from the robot to a vision target that is at an angle
 */
public class LimelightAngularToDistanceComponent implements DoubleSupplier {
    /**
     * The supplier that determines the angle at which the Limelight is positioned
     */
    @NotNull
    DoubleSupplier angularInput;
    /**
     * The supplier determining the distance to the vision target
     */
    @NotNull
    DoubleSupplier distanceToTarget;

    /**
     * Default constructor
     * @param angularInput The DoubleSupplier determining the angle theta of the vision target
     * @param distanceToTarget The LimeLightComponent finding the distance to the vision target
     */
    @JsonCreator
    public LimelightAngularToDistanceComponent(@JsonProperty(required = true) DoubleSupplier angularInput,
                                               @JsonProperty(required = true) DoubleSupplier distanceToTarget){
        this.angularInput = angularInput;
        this.distanceToTarget = distanceToTarget;
    }

    /**
     * Determines the distance from the robot to the vision target using the angle from angularInput
     * @return
     */
    @Override
    public double getAsDouble() {
        double theta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        return distanceToTarget.getAsDouble() * Math.cos(Math.toRadians(theta)) * Math.tan(Math.toRadians(angularInput.getAsDouble()));
    }
}
