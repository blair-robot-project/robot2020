package org.usfirst.frc.team449.robot.generalInterfaces.limelight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public class Limelight extends SubsystemBase implements Loggable {

  /** whether the limelight can see a valid target */
  private final NetworkTableEntry validTargetTable;
  /** x offset. in degrees, of the target from the crosshair */
  private final NetworkTableEntry xTable;
  /** y offset. in degrees, of the target from the crosshair */
  private final NetworkTableEntry yTable;
  /** area of the target, as a percent of the camera screen */
  private final NetworkTableEntry areaTable;
  /** rotation (-90 to 0, in degrees) of the target (as the limelight sees it) */
  private final NetworkTableEntry skewTable;
  /** the pipeline's latency contribution, in ms */
  private final NetworkTableEntry latencyTable;
  /** sidelength of the shortest side of the vision target box, in pixels */
  private final NetworkTableEntry shortTable;
  /** sidelength of the longest side of the vision target box, in pixels */
  private final NetworkTableEntry longTable;
  /** width of target box, in pixels */
  private final NetworkTableEntry widthTable;
  /** height of target box, in pixels */
  private final NetworkTableEntry heightTable;
  /** pipeline index of the limelight */
  private final NetworkTableEntry pipeTable;
  /** camtran, for getting 3D pos */
  private final NetworkTableEntry camtran;
  /** pipeline for driver camera */
  private int driverPipeline;
  private boolean validTarget;
  private double x;
  private double y;
  private double area;
  private double skew;
  private double latency;
  private double shortest;
  private double longest;
  private double width;
  private double height;
  private int pipeIndex;

  // The possible camtran values
  /** the xPose of the robot in camtran */
  private double poseX;
  /** same but for y */
  private double poseY;
  /** same but for z */
  private double poseZ;
  /** up-down angle of the robot in camtran. Look it up if confused */
  private double pitch;
  /** side-to-side angle. Look it up if confused */
  private double yaw;
  /** rotation angle. Look it up if confused */
  private double roll;

  /**
   * Default constructor
   *
   * @param driverPipeline the pipeline for the driver camera
   */
  @JsonCreator
  public Limelight(int driverPipeline) {
    this.driverPipeline = driverPipeline;
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    validTargetTable = table.getEntry("tv");
    xTable = table.getEntry("tx");
    yTable = table.getEntry("ty");
    areaTable = table.getEntry("ta");
    skewTable = table.getEntry("ts");
    latencyTable = table.getEntry("tl");
    shortTable = table.getEntry("tshort");
    longTable = table.getEntry("tlong");
    widthTable = table.getEntry("thor");
    heightTable = table.getEntry("tvert");
    pipeTable = table.getEntry("getpipe");
    camtran = table.getEntry("camtran");
    setPipeline(driverPipeline);
  }

  @Override
  public void periodic() {
    pipeIndex = (int) pipeTable.getDouble(driverPipeline);
    if (pipeIndex != driverPipeline) {
      validTarget = validTargetTable.getBoolean(false);
      x = xTable.getDouble(0);
      y = yTable.getDouble(0);
      //            area = areaTable.getDouble(0);
      //            skew = skewTable.getDouble(0);
      //            latency = latencyTable.getDouble(0);
      //            shortest = shortTable.getDouble(0);
      //            longest = longTable.getDouble(0);
      //            width = widthTable.getDouble(0);
      //            height = heightTable.getDouble(0);
      //            double[] camtranVals = camtran.getDoubleArray(new double[6]);
      //            poseX = camtranVals[0];
      //            poseY = camtranVals[1];
      //            poseZ = camtranVals[2];
      //            pitch = camtranVals[3];
      //            yaw = camtranVals[4];
      //            roll = camtranVals[5];
    }
  }

  public boolean hasTarget() {
    return validTarget;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getArea() {
    return area;
  }

  public double getSkew() {
    return skew;
  }

  public double getLatency() {
    return latency;
  }

  public double getShortest() {
    return shortest;
  }

  public double getLongest() {
    return longest;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public double getPipeline() {
    return pipeIndex;
  }

  public void setPipeline(int index) {
    pipeTable.setNumber(index);
  }

  public double getPoseX() {
    return poseX;
  }

  public double getPoseY() {
    return poseY;
  }

  public double getPoseZ() {
    return poseZ;
  }

  public double getPitch() {
    return pitch;
  }

  public double getYaw() {
    return yaw;
  }

  public double getRoll() {
    return roll;
  }
}
