package org.usfirst.frc.team449.robot.components;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.DoubleSupplier;

public class LimeLighDistanceComponentSimple implements DoubleSupplier {

    @JsonCreator
    public LimeLighDistanceComponentSimple(){

    }

    @Override
    public double getAsDouble() { //Actual target height 5.825572
        double robotToTargAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tvert").getDouble(0) * (41./320.);
        double distanceToTarg = (Math.sqrt(137.)/2.) / (2. * Math.tan(Math.toRadians(robotToTargAngle / 2.)));
        return distanceToTarg;
    }
}
