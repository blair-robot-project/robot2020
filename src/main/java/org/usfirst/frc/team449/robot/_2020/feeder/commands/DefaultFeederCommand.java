package org.usfirst.frc.team449.robot._2020.feeder.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.Loggable;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot._2020.shooter.FlywheelWithTimeout;
import org.usfirst.frc.team449.robot.components.ConditionTimingComponentDecorator;
import org.usfirst.frc.team449.robot.other.Clock;

/**
 * Runs feeder when shooting and feeder (along with index wheel) when indexing sensor tripped.
 */
public class DefaultFeederCommand extends CommandBase implements Loggable {
    @NotNull
    private final SubsystemIntake.IntakeMode transitionWheelIndexingMode;
    @NotNull
    private final SubsystemIntake.IntakeMode transitionWheelShootingMode;
    @NotNull
    private final SubsystemIntake.IntakeMode feederIndexingMode;
    @NotNull
    private final SubsystemIntake.IntakeMode feederShootingMode;
    @NotNull
    private final SubsystemIntake.IntakeMode feederCoughingMode;

    @Nullable
    private final Double indexingTimeout;

    @NotNull
    private final SubsystemIntake feeder;
    @NotNull
    private final SubsystemIntake bumper;
    @NotNull
    private final SubsystemIntake transitionWheel;
    @NotNull
    private final FlywheelWithTimeout shooter;

    @NotNull
    private final ConditionTimingComponentDecorator indexingSensor, indexing, shooting, flywheelOn;

    /**
     * Default constructor
     *
     * @param transitionWheelIndexingMode the {@link SubsystemIntake.IntakeMode} to run the transition
     *                                    wheel at when indexing
     * @param transitionWheelShootingMode the {@link SubsystemIntake.IntakeMode} to run the transition
     *                                    wheel at when shooting
     * @param feederIndexingMode          the {@link SubsystemIntake.IntakeMode} to run the feeder at when
     *                                    indexing
     * @param feederCoughingMode          the {@link SubsystemIntake.IntakeMode} that the feeder will be in
     *                                    when it is coughing. Used so the command will cease action while coughing takes place
     * @param feederShootingMode          the {@link SubsystemIntake.IntakeMode} to run the feeder at when
     *                                    indexing
     * @param sensor                      the first sensor of the transition from intake to feeder
     * @param transitionWheel             the transition wheel of the intake
     * @param feeder                      the feeder
     * @param shooter                     the shooter
     * @param indexingTimeout             maximum duration for which to keep running the feeder if the sensors
     *                                    remain continuously activated
     */
    @JsonCreator
    public DefaultFeederCommand(
            @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode transitionWheelIndexingMode,
            @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode transitionWheelShootingMode,
            @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode feederIndexingMode,
            @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode feederCoughingMode,
            @NotNull @JsonProperty(required = true) final SubsystemIntake.IntakeMode feederShootingMode,
            @NotNull @JsonProperty(required = true) final BooleanSupplier sensor,
            @NotNull @JsonProperty(required = true) final SubsystemIntake bumper,
            @NotNull @JsonProperty(required = true) final SubsystemIntake transitionWheel,
            @NotNull @JsonProperty(required = true) final SubsystemIntake feeder,
            @NotNull @JsonProperty(required = true) final FlywheelWithTimeout shooter,
            @Nullable final Double indexingTimeout) {
        this.transitionWheelIndexingMode = transitionWheelIndexingMode;
        this.transitionWheelShootingMode = transitionWheelShootingMode;
        this.feederIndexingMode = feederIndexingMode;
        this.feederCoughingMode = feederCoughingMode;
        this.feederShootingMode = feederShootingMode;

        this.bumper = bumper;
        this.transitionWheel = transitionWheel;
        this.feeder = feeder;
        this.shooter = shooter;

        this.indexingTimeout = indexingTimeout;

        this.flywheelOn = new ConditionTimingComponentDecorator(shooter::isFlywheelOn, false);
        this.shooting = new ConditionTimingComponentDecorator(shooter::isConditionTrueCached, false);
        this.indexingSensor = new ConditionTimingComponentDecorator(sensor, false);
        this.indexing = new ConditionTimingComponentDecorator(() -> {
            // Give up if it's been long enough after either sensor last activated and there's still something
            // activating one of them. This specifically will continue giving up even if one of the sensors
            // deactivates but the other still surpasses the timeout.
            if (this.indexingTimeout != null && this.indexingSensor.timeSinceLastBecameTrue() > this.indexingTimeout) {
                return false;
            }

            // Run when either sensor is being actively tripped.
            return this.indexingSensor.isTrue();
        }, false);
    }

    @Override
    public void execute() {
        final double currentTime = Clock.currentTimeSeconds();

        this.indexingSensor.update(currentTime);
        this.indexing.update(currentTime);
        this.shooting.update(currentTime);
        this.flywheelOn.update(currentTime);

        if (this.feeder.getMode() == feederCoughingMode) {
            return;
        }

        if (this.shooting.isTrue()) {
            this.transitionWheel.setMode(this.transitionWheelShootingMode);
            this.feeder.setMode(this.feederShootingMode);

            return;
        }

        if (this.shooting.justBecameFalse()) {
            this.feeder.setMode(SubsystemIntake.IntakeMode.OFF);
            this.transitionWheel.setMode(SubsystemIntake.IntakeMode.OFF);
        }

        if (this.flywheelOn.isTrue()) {
            this.feeder.setMode(SubsystemIntake.IntakeMode.OFF);

            // Also turn off the intake to prevent jamming if it's on and a ball gets in somehow.
            this.bumper.setMode(SubsystemIntake.IntakeMode.OFF);
            this.transitionWheel.setMode(SubsystemIntake.IntakeMode.OFF);

            // Indexing will time out if not being able to shoot is why the transition wheel was stopped,
            // so we must pretend that the sensor has just activated if it was already activated while shooting.
            this.indexingSensor.forceUpdate(currentTime, this.indexingSensor.isTrue());

            return;
        }

        if (this.indexing.justBecameFalse()) {
            this.feeder.setMode(SubsystemIntake.IntakeMode.OFF);

            return;
        }

        if (this.indexing.justBecameTrue()) {
            this.transitionWheel.setMode(this.transitionWheelIndexingMode);
            this.feeder.setMode(this.feederIndexingMode);
        }
    }
}
