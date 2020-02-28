package org.usfirst.frc.team449.robot._2020.feeder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.LinkedList;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.shooter.SubsystemFlywheel;
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentDecorator;
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentObserver;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.other.Clock;

import static org.usfirst.frc.team449.robot.other.Util.getLogPrefix;

/**
 * Yes, this is a subsystem in order to perform updates in {@link BallCounter#periodic()}.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class BallCounter extends SubsystemBase implements Loggable {
  private static final double BUFFER_DIFF_THRESHOLD = 1;
  private static final int MAX_BALLS = 5;
  private static final int FLYWHEEL_SPEED_BUFFER_SIZE = 5;

  @NotNull
  private final ConditionTimingComponentDecorator sensor1;
  @NotNull private final ConditionTimingComponentDecorator sensor2;
  @NotNull private final ConditionTimingComponentObserver isSpeedDecliningAfterShot;
  @NotNull private final SubsystemFlywheel flywheel;
  @Log
  private int betweenSensors, insideFeeder;
  @Log.ToString
  @NotNull private final LinkedList<Double> speedBuffer = new LinkedList<>();

  @JsonCreator
  public BallCounter(@NotNull @JsonProperty(required = true) final MappedDigitalInput sensor1,
                     @NotNull @JsonProperty(required = true) final MappedDigitalInput sensor2,
                     @NotNull @JsonProperty(required = true) final SubsystemFlywheel flywheel) {
    this.sensor1 = new ConditionTimingComponentDecorator(sensor1, false);
    this.sensor2 = new ConditionTimingComponentDecorator(sensor2, false);
    isSpeedDecliningAfterShot = new ConditionTimingComponentObserver(false);
    this.flywheel = flywheel;

    if (this.flywheel.getSpeed().isEmpty())
      throw new IllegalArgumentException("Can't use flywheel that doesn't provide speed.");
  }

  @Override
  public void periodic() {
    final double currentTime = Clock.currentTimeSeconds();
    this.sensor1.update(currentTime);
    this.sensor2.update(currentTime);

    if (this.sensor1.justBecameTrue()) {
      this.betweenSensors++;
    }

    if (this.sensor2.justBecameTrue()) {
      this.betweenSensors--;
      this.insideFeeder++;
    }

    //noinspection OptionalGetWithoutIsPresent
    this.speedBuffer.addFirst(this.flywheel.getSpeed().get());
    if (this.speedBuffer.size() > FLYWHEEL_SPEED_BUFFER_SIZE) {
      this.speedBuffer.removeLast();
    }

    this.isSpeedDecliningAfterShot.update(currentTime,
        this.flywheel.getFlywheelState() == SubsystemFlywheel.FlywheelState.SHOOTING &&
            this.speedBuffer.getFirst() - this.speedBuffer.getLast() < -BUFFER_DIFF_THRESHOLD);
    if (this.isSpeedDecliningAfterShot.justBecameTrue()) {
      this.insideFeeder--;
    }

    if (this.betweenSensors < 0) {
      this.betweenSensors = 0;
      System.err.println(getLogPrefix(this) + "Second sensor activated but not first sensor");
    }
    if (this.insideFeeder < 0) {
      this.insideFeeder = 0;
      System.err.println(getLogPrefix(this) + "Shot reported with no balls in feeder");
    }
    if (this.betweenSensors + this.insideFeeder > MAX_BALLS) {
      System.err.println(getLogPrefix(this) + "More than 5 balls in system");
    }
  }
}
