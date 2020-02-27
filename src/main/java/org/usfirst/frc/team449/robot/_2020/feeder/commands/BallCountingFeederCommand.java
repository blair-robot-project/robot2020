package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake.IntakeMode;

/** A feeder that counts balls */
@JsonIdentityInfo(generator = StringIdGenerator.class)
public class BallCountingFeederCommand extends CommandBase implements Loggable {

  private final SubsystemIntake feeder;
  private final BooleanSupplier sensor1, sensor2;
  private final int ballThreshold;
  private final IntakeMode defaultMode;
  // private final double updateTimeMillis;

  /** The previous values from the IR sensors */
  private boolean sensor1Cached, sensor2Cached;
  /** The current number of balls inside the robot */
  private int numBalls;

  /**
   * @param feeder the feeder feeder to operate on
   * @param sensor1 the first sensor of the transition from intake to feeder
   * @param sensor2 the second sensor of the transition from intake to feeder
   * @param defaultMode The default intake mode
   * @param ballThreshold The number of balls that it will shoot at once before waiting /@param
   *     updateTimeMillis The time, in millisecs, to wait to update
   */
  @JsonCreator
  public BallCountingFeederCommand(
      @NotNull SubsystemIntake feeder,
      @NotNull BooleanSupplier sensor1,
      @NotNull BooleanSupplier sensor2,
      @NotNull @JsonProperty(required = true) IntakeMode defaultMode,
      @JsonProperty(required = true) int ballThreshold
      /*@JsonProperty(required = true) double updateTimeMillis*/ ) {
    // todo consider using generics
    addRequirements((Subsystem) feeder);

    this.feeder = feeder;
    this.sensor1 = sensor1;
    this.sensor2 = sensor2;
    this.ballThreshold = ballThreshold;
    this.defaultMode = defaultMode;

    sensor1Cached = sensor1.getAsBoolean();
    sensor2Cached = sensor2.getAsBoolean();
    // this.updateTimeMillis = updateTimeMillis;
  }

  @Override
  public synchronized void execute() {
    boolean sensor1Now = sensor1.getAsBoolean(), sensor2Now = sensor2.getAsBoolean();
    // var mode = feeder.getMode();
    if (sensor2Cached
        && !sensor2
            .getAsBoolean() /*&& mode == IntakeMode.OUT_FAST && shooter.isReadyToShoot()*/) {
      // the ball(s) was/were there and now it's/they've moved on, probably been shot,
      //  since OUT_FAST means it's at shooting speed
      //  We can restart the count now (unless only one ball got shot, which probably
      // happened).
      // TODO find a better way to figure out if balls were shot out (use the flywheel
      //   maybe)?
      numBalls = 0;
    } else if (sensor1Cached && !sensor1.getAsBoolean()) {
      // The feeder has managed to trap a ball in its jaws
      // We can assume it's only one ball at a time, because it's that slow
      numBalls++;
    }

    /*
     * If it's taking in balls and it's over the limit, stop
     * If it's not supposed to be running, stop
     */
    if ((numBalls > ballThreshold && feeder.getMode() == IntakeMode.IN_FAST)
        || !shouldBeRunning()) {
      feeder.setMode(IntakeMode.OFF);
    } else {
      feeder.setMode(defaultMode);
    }

    //Update sensor values
    sensor1Cached = sensor1Now;
    sensor2Cached = sensor2Now;
  }

  public boolean shouldBeRunning() {
    return sensor1.getAsBoolean() || sensor2.getAsBoolean();
  }
}
