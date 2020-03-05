package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemConditional;

/** A subsystem with a flywheel and feeder. */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public interface SubsystemFlywheel extends SubsystemConditional {

  /** Turn the flywheel on to the specified speed. */
  void turnFlywheelOn(double speed);

  /** Turn the flywheel off. */
  void turnFlywheelOff();

  /** @return Expected time from giving the flywheel voltage to being ready to fire, in seconds. */
  double getSpinUpTimeSecs();

  /** @return Whether the flywheel has attained a speed specified to be sufficient for shooting. */
  boolean isReadyToShoot();

  @Override
  default boolean isConditionTrue() {
    return this.isReadyToShoot();
  }

  @NotNull
  default Optional<Double> getSpeed() {
    return Optional.empty();
  }
}
