package org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands;

import edu.wpi.first.wpilibj2.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.usfirst.frc.team449.robot.CommandContainer;
import org.usfirst.frc.team449.robot.commands.general.MappedSequentialCommandGroup;
import org.usfirst.frc.team449.robot.commands.general.MappedWaitCommand;
import org.usfirst.frc.team449.robot.commands.general.ConditionalCommandFunctional;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands.TurnAllOn;
import org.usfirst.frc.team449.robot.subsystem.complex.shooter.LoggingFlywheel;
import org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel.commands.SpinUpFlywheel;
import org.usfirst.frc.team449.robot.subsystem.interfaces.intake.IntakeSimple;
import org.usfirst.frc.team449.robot.subsystem.interfaces.intake.SubsystemIntake;

import java.util.*;

/**
 * Just all the stuff Yueqin added to the map for the feeder
 * put into one command, for ease of readability
 * TODO test this out
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SpinUpFlywheelUntilIntakeStops extends SequentialCommandGroup implements ISpinUpFlywheelCommand {

    Command x;
    /**
     *
      * @param timeToStart how long the feeder waits to start. Not sure how necessary this is
     * @param spinUpFlywheelCommand
     * @param shooterFlywheel
     * @param intake the intake
     * @param timeToStop how long the feeder keeps running after intake stops, in sec
     */
  public SpinUpFlywheelUntilIntakeStops(@Nullable Double timeToStart,
                                        @NotNull final SpinUpFlywheel spinUpFlywheelCommand,
                                        @NotNull final LoggingFlywheel shooterFlywheel,
                                        @NotNull final IntakeSimple intake,
                                        @Nullable Double timeToStop) {
    super(new MappedWaitCommand(timeToStart == null ? 1.0 : timeToStart),
            makePerpetualCommand(spinUpFlywheelCommand, shooterFlywheel, intake, timeToStop));
  }

  public static Command makePerpetualCommand(@NotNull final SpinUpFlywheel spinUpFlywheelCommand,
                   @NotNull final LoggingFlywheel shooterFlywheel,
                   @NotNull final IntakeSimple intake,
                   @Nullable Double timeToStop) {
      //todo should this be a perpetual command?
      return new PerpetualCommand(
              //TODO make this less complicated, this is even worse than before
              new ConditionalCommandFunctional(new CommandBase() {
                  Runnable endCommand = () -> {
                          this.end(false);
                  };
                  @Override
                  public void execute() {
                      //stop after timeToStop seconds
                      Timer timer = new Timer();
                      timer.schedule(new TimerTask() {
                          public void run() {
                            //TODO is it interrupted? Does making it false keep it from starting up again?
                            endCommand.run();
                          }
                      }, (long) (timeToStop == null ? 3000 : timeToStop * 1000L));
                  }
              }, new ConditionalCommandFunctional(
                      new TurnAllOn(shooterFlywheel),
                      spinUpFlywheelCommand,
                      shooterFlywheel::isConditionTrueCached),
                      //whether or not it is off
                      () -> intake.getMode().compareTo(SubsystemIntake.IntakeMode.OFF) == 0)
      );
  }

}
