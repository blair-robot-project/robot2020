package org.usfirst.frc.team449.robot.components;

        import com.fasterxml.jackson.annotation.JsonCreator;
        import com.fasterxml.jackson.annotation.JsonIdentityInfo;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.ObjectIdGenerators;
        import org.jetbrains.annotations.NotNull;
        import org.usfirst.frc.team449.robot.jacksonWrappers.MappedAHRS;

        import java.util.function.DoubleSupplier;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class PhiCalculatorComponent implements DoubleSupplier {

    @NotNull
    private MappedAHRS ahrs;

    @JsonCreator
    public PhiCalculatorComponent (@JsonProperty (required = true) MappedAHRS ahrs){

    }

    @Override
    public double getAsDouble() {
        return 0;
    }
}
