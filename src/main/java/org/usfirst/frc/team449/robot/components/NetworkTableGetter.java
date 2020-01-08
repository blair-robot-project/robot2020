package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.InstantCommand;


public class NetworkTableGetter{

    private NetworkTable netTable;

    public NetworkTableGetter(){
        netTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public double tableGet(String param){
        return netTable.getEntry(param).getDouble(0);
    }
    public double validTargets(){
        return tableGet("tv");
    }
    public double getXOffset(){
        return tableGet("tx");
    }
    public double getYOffset(){
        return tableGet("ty");
    }
    public double getTargetArea(){
        return tableGet("ta");
    }
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
