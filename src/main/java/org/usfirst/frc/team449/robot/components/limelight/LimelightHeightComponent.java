package org.usfirst.frc.team449.robot.components.limelight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

/** Finds the height of a vision target above the limelight. Not entirely certain if it works...? */
public class LimelightHeightComponent implements DoubleSupplier {
  /**
   * The supplier that determines the angle of the target above or below the Limelight. Should be a
   * LimelightComponent with ReturnValue y.
   */
  @NotNull final DoubleSupplier angularInput;
  /**
   * The supplier determining the distance to the vision target. Should be a
   * LimelightDistanceComponent.
   */
  @NotNull final DoubleSupplier distanceToTarget;

  /**
   * Default constructor
   *
   * @param angularInput The DoubleSupplier determining the angle theta of the vision target.
   * @param distanceToTarget The LimelightComponent finding the distance to the vision target.
   */
  @JsonCreator
  public LimelightHeightComponent(
      @NotNull @JsonProperty(required = true) DoubleSupplier angularInput,
      @NotNull @JsonProperty(required = true) DoubleSupplier distanceToTarget) {
    this.angularInput = angularInput;
    this.distanceToTarget = distanceToTarget;
  }

  /** @return the height of the vision target above the limelight */
  @Override
  public double getAsDouble() {
    double theta =
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    return distanceToTarget.getAsDouble()
        * Math.cos(Math.toRadians(theta))
        * Math.tan(Math.toRadians(angularInput.getAsDouble()));
  }
}
