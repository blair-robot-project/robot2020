package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.DoubleSupplier;

public class LimeLightDistanceComponentTilted implements DoubleSupplier {

    private final double limelightHeight;

    private final double limelightAngleDown;

    @JsonCreator
    public LimeLightDistanceComponentTilted(@JsonProperty(required = true) double limelightHeight,
                                            @JsonProperty(required = true) double limelightAngleDown) {
        this.limelightHeight = limelightHeight;
        this.limelightAngleDown = limelightAngleDown;
    }

    @Override
    public double getAsDouble() {
        double robotToTargAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        return limelightHeight * Math.tan(Math.toRadians(90 - limelightAngleDown + robotToTargAngle));
    }
}
