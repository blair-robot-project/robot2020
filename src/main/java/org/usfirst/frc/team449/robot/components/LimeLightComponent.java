package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.jetbrains.annotations.NotNull;
import java.util.function.DoubleSupplier;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LimeLightComponent implements DoubleSupplier {

    @NotNull NetworkTableEntry entry;
    private final NetworkTableEntry tv;
    private final ReturnValue value;
    private final double offset;

    enum ReturnValue {
        x, y, area, poseX, poseY, poseZ, pitch, yaw, roll
    }

    /**
     * Default creator
     * @param value whether to request x distance from center, y distance from center, or the area of vision target
     */
    @JsonCreator
    public LimeLightComponent(@JsonProperty(required = true) ReturnValue value,
                              double offset){
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        this.value = value;
        this.offset = offset;
        switch(value) {
            case x:
                entry = table.getEntry("tx");
                break;
            case y:
                entry = table.getEntry("ty");
                break;
            case area:
                entry = table.getEntry("ta");
                break;
            default:
                entry = table.getEntry("camtran");
        }
        tv = table.getEntry("tv");
    }

    /**
     * @return requested value from LimeLight
     */
    @Override
    public double getAsDouble() {
        if (tv.getDouble(0) == 0){
            return Double.NaN;
        }
        double[] camtran = entry.getDoubleArray(new double[6]);
        switch(value) {
            case poseX:
                return camtran[0] + offset;
            case poseY:
                return camtran[1] + offset;
            case poseZ:
                return camtran[2] + offset;
            case pitch:
                return camtran[3] + offset;
            case yaw:
                return camtran[4] + offset;
            case roll:
                return camtran[5] + offset;
            default:
                return entry.getDouble(0.0) + offset;
        }
    }
}