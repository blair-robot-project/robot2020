package org.usfirst.frc.team449.robot.commands.multiInterface.limelight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.commands.limelight.SetPipeline;
import org.usfirst.frc.team449.robot.commands.multiInterface.drive.NavXTurnToAngleLimelight;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.limelight.Limelight;

/**
 * Turns on the limelight LEDs and starts scanning for a target When one is found, it overrides the
 * default drive command and turns to that target
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ScanThenTurn extends CommandBase implements Loggable {

  private final Limelight limelight;

  private final int scannerPipe;
  private final NavXTurnToAngleLimelight turnCommand;
  private final int driverPipe;

  private double cachedAngle;

  /**
   * Default constructor
   *
   * @param scannerPipe the pipeline index used while scanning
   * @param turnCommand the command that uses the limelight (turn to vision, drive to vision, etc)
   *     as of 2020, only one is turnToAngleLimelight, so that's what this should be
   * @param driverPipe the pipeline index that turns the LEDs off and which the driver uses as a
   *     camera
   */
  @JsonCreator
  public ScanThenTurn(
      @JsonProperty(required = true) int scannerPipe,
      @NotNull @JsonProperty(required = true) NavXTurnToAngleLimelight turnCommand,
      @JsonProperty(required = true) int driverPipe,
      @JsonProperty(required = true) Limelight limelight) {
    this.limelight = limelight;
    this.scannerPipe = scannerPipe;
    this.turnCommand = turnCommand;
    this.driverPipe = driverPipe;
  }

  @Override
  public void initialize() {
    new SetPipeline(limelight, scannerPipe).schedule();
  }

  @Override
  public boolean isFinished() {
    return limelight.hasTarget();
  }

  @Override
  public void end(boolean interrupted) {
    cachedAngle = limelight.getX();
    new SetPipeline(limelight, driverPipe).schedule();
    if (!interrupted) {
      turnCommand.schedule();
    }
  }
}
