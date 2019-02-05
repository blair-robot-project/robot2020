package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedAHRS;
import java.util.function.DoubleSupplier;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class LimelightNavXTargetComponent implements DoubleSupplier {

    @NotNull
    private MappedAHRS ahrs;

    @JsonCreator
    public LimelightNavXTargetComponent(@JsonProperty (required = true) MappedAHRS ahrs){
        this.ahrs = ahrs;
    }

    @Override
    public double getAsDouble() {
        double heading = ahrs.getHeading();
        if (heading >= 0){
            if (heading % 45 < Math.abs(heading % 45 - 45)) return heading % 45 / 2;
            else return (heading % 45 - 45) / 2;
        }
        /*
         * May want to negate answers. Either by negating return statements, or by treating the angle as positive (+) originally
         * and switching back to negative (-) later
         */
        else {
            if (heading % 45 + 45 < Math.abs(heading % 45)) return (heading % 45 + 45) / 2;
            else return heading % 45 / 2;
        }
    }
}
