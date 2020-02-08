package org.usfirst.frc.team449.robot.components;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Has all of the methods for getting any desired value from the limelight.
 * Poorly documented, reference docs.limelightvision.io/en/latest/networktables_api.html for full documentation
 * Also THIS IS OBSOLETE, should be using the LimelightComponent class for the capacity to tailor offsets
 */
public class Limelight {

    /**
     * The network table to get the values from
     */
    private NetworkTable netTable;

    public Limelight(){
        netTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    /**
     * Gets the value from the corresponding param from netTable.
     * Used exclusively to avoid typing "netTable.getEntry(param).getDouble(0)" for every method
     * @param param the value to ask for from the limelight
     * @return the value asked for
     */
    private double tableGet(String param){
        return netTable.getEntry(param).getDouble(0);
    }

    /**
     * @return whether the limelight can see valid targets (0 or 1)
     */
    public double validTargets(){
        return tableGet("tv");
    }

    /**
     * @return horizontal offset of target (as an angle)
     */
    public double getXOffset(){
        return tableGet("tx");
    }

    /**
     * @return vertical offset from crosshair to target (as an angle)
     */
    public double getYOffset(){
        return tableGet("ty");
    }

    /**
     * @return area of target (as a percent of the image onscreen)
     */
    public double getTargetArea(){
        return tableGet("ta");
    }

    /**
     * @return skew or rotation (-90 to 0, in degrees)
     */
    public double getSkew(){
        return tableGet("ts");
    }
    public double getLatency(){
        return tableGet("tl");
    }
    public double getShortest(){
        return tableGet("tshort");
    }
    public double getLongest(){
        return tableGet("tlong");
    }
    public double getHorizontalSideLen(){
        return tableGet("thor");
    }
    public double getVerticalSideLen(){
        return tableGet("tvert");
    }
    public double getPipelineIndex(){
        return tableGet("getpipe");
    }
    public double getCamTran(){
        return tableGet("camtran");
    }

}
