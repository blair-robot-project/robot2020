package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;
public class LimeLightAngularToDistanceComponent implements DoubleSupplier {
    @NotNull
    DoubleSupplier angularInput;

    @NotNull
    DoubleSupplier distanceToTarget;

    @JsonCreator
    public LimeLightAngularToDistanceComponent(@JsonProperty(required = true) DoubleSupplier angularInput,
                                               @JsonProperty(required = true) DoubleSupplier distanceToTarget){
        this.angularInput = angularInput;
        this.distanceToTarget = distanceToTarget;
    }

    @Override
    public double getAsDouble() {
        double theta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        return distanceToTarget.getAsDouble() * Math.cos(theta) * Math.tan(angularInput.getAsDouble());
    }
}
