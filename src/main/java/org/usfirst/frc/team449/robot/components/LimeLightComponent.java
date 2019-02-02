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

    enum returnValue{
        x,y,area;
    }

    @JsonCreator
    public LimeLightComponent(@JsonProperty(required = true) returnValue value){
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        if(value == returnValue.x) {
            entry = table.getEntry("tx");
        }else if(value == returnValue.y){
            entry = table.getEntry("ty");
        }else{
            entry = table.getEntry("ta");
        }
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public double getAsDouble() {
        return entry.getDouble(0.0)
                * NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0);
    }
}