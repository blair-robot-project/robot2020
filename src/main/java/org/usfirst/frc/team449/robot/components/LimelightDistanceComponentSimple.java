package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.DoubleSupplier;

/**
 * Determines the distance from the Limelight to a vision target. Currently using a ton of hardcoded values.
 * Assumes that the Limelight is not tilted with respect to the vision targets
 * If the Limelight is tilted, use LimeLightDistanceComponentTilted
 */
public class LimelightDistanceComponentSimple implements DoubleSupplier {

    /**
     * Default constructor
     */
    @JsonCreator
    public LimelightDistanceComponentSimple(){

    }

    /**
     * @return the distance for the robot to travel to the vision target, assuming that it isn't angled
     */
    @Override
    public double getAsDouble() { //Actual target height 5.825572
        double robotToTargAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tvert").getDouble(0) * (41./320.);
        double distanceToTarg = (Math.sqrt(137.)/2.) / (2. * Math.tan(Math.toRadians(robotToTargAngle / 2.)));
        return distanceToTarg;
    }
}
