package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.DoubleSupplier;

/**
 * Determines the distance from the Limelight to a vision target.
 */
public class LimelightDistanceComponent implements DoubleSupplier {

    /**
     * The height of the Limelight
     */
    private final double limelightHeight;
    /**
     * The mounting angle above the horizontal of the limelight, in degrees
     */
    private final double limelightAngle;
    /**
     *
     */
    private final double targetHeight;

    /**
     * Default constructor
     *
     * @param limelightHeight The height of the Limelight
     * @param limelightAngleDown The angle of the Limelight, in degrees
     * @param targetHeight the height of the expected vision target, provided by the game manual
     */
    @JsonCreator
    public LimelightDistanceComponent(@JsonProperty(required = true) double limelightHeight,
                                      @JsonProperty(required = true) double limelightAngleDown,
                                      @JsonProperty(required = true) double targetHeight) {
        this.limelightHeight = limelightHeight;
        this.limelightAngle = limelightAngleDown;
        this.targetHeight = targetHeight;
    }

    /**
     * @return Gets the distance from the robot to the vision target, coplanar with the field
     */
    @Override
    public double getAsDouble() {
        double robotToTargAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        return (targetHeight - limelightHeight) * Math.tan(Math.toRadians(limelightAngle + robotToTargAngle));
    }
}
