package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemConditional;

import java.util.Optional;

/**
 * A subsystem with a flywheel and feeder.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public interface SubsystemFlywheel extends SubsystemConditional {

  /**
   * Turn the flywheel on to the speed passed to the constructor.
   */
  void turnFlywheelOn();

  /**
   * Turn the flywheel off.
   */
  void turnFlywheelOff();

  /**
   * @return The current state of the flywheel.
   */
  @NotNull
  FlywheelState getFlywheelState();

  /**
   * @param state The state to switch the flywheel to.
   */
  void setFlywheelState(@NotNull FlywheelState state);

  /**
   * @return Expected time from giving the flywheel voltage to being ready to fire, in seconds.
   */
  double getSpinUpTimeSecs();

  /**
   * @return Whether the flywheel has attained a speed specified to be sufficient for shooting.
   */
  default boolean isReadyToShoot() {
    return true;
  }

  @NotNull
  default Optional<Double> getSpeed() {
    return Optional.empty();
  }

  /**
   * An enum for the possible states of the flywheel.
   */
  enum FlywheelState {
    // Both flywheel and feeder off
    OFF,
    // Feeder off, flywheel on
    SPINNING_UP,
    // Both flywheel and feeder on
    SHOOTING
  }
}
