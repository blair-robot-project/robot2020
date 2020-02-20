package org.usfirst.frc.team449.robot.subsystem.complex.intake;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveUnidirectional;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;
import org.usfirst.frc.team449.robot.other.Util;
import org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.SubsystemAnalogMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.intake.SubsystemIntake;

import java.util.HashMap;
import java.util.Map;

/**
 * An intake that consists of rollers that are regulated to a consistent velocity relative to the
 * ground.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeGroundSpeedRegulating extends SubsystemBase implements SubsystemIntake, SubsystemAnalogMotor, Loggable {
  @NotNull
  private final DriveUnidirectional drive;
  @NotNull
  private final SmartMotor motor;
  private final double minVelFraction;
  @Log.ToString
  @NotNull
  private IntakeMode mode = IntakeMode.OFF;
  @NotNull
  private final Map<IntakeMode, Double> targetSpeeds;

  /**
   * Default constructor
   *
   * @param drive The drive subsystem from which to obtain ground speed information.
   * @param motor The motor this subsystem controls.
   * @param inSlowVel The velocity for the motor to go at for the IN_SLOW {@link
   * SubsystemIntake.IntakeMode} in FPS. Can be null to indicate that this intake doesn't have/use
   * IN_SLOW.
   * @param inFastVel The velocity for the motor to go at for the IN_FAST {@link
   * SubsystemIntake.IntakeMode} in FPS. Can be null to indicate that this intake doesn't have/use
   * IN_FAST.
   * @param outSlowVel The velocity for the motor to go at for the OUT_SLOW {@link
   * SubsystemIntake.IntakeMode} in FPS. Can be null to indicate that this intake doesn't have/use
   * OUT_SLOW.
   * @param outFastVel The velocity for the motor to go at for the OUT_FAST {@link
   * SubsystemIntake.IntakeMode} in FPS. Can be null to indicate that this intake doesn't have/use
   * OUT_FAST.
   * @param minVelFraction The minimum fraction of the target speed for the current mode to run at
   * when slowing down due to robot movement.
   * <p>
   * Sign is not ignored. Zero prevents the intake from reversing, and negative values allow it to
   * reverse if the robot attains a higher speed than the target speed.
   * </p>
   */
  @JsonCreator
  public IntakeGroundSpeedRegulating(@JsonProperty(required = true) @NotNull final DriveUnidirectional drive,
                                     @JsonProperty(required = true) @NotNull final SmartMotor motor,
                                     @Nullable final Double inSlowVel,
                                     @Nullable final Double inFastVel,
                                     @Nullable final Double outSlowVel,
                                     @Nullable final Double outFastVel,
                                     final double minVelFraction) {
    this.drive = drive;
    this.motor = motor;
    this.minVelFraction = minVelFraction;

    this.targetSpeeds = new HashMap<>();
    targetSpeeds.put(IntakeMode.OFF, 0.0);
    targetSpeeds.put(IntakeMode.IN_SLOW, inSlowVel);
    targetSpeeds.put(IntakeMode.IN_FAST, inFastVel);
    targetSpeeds.put(IntakeMode.OUT_SLOW, outSlowVel);
    targetSpeeds.put(IntakeMode.OUT_FAST, outFastVel);
  }

  @Override
  public void setMode(@NotNull final IntakeMode mode) {
    if (this.targetSpeeds.get(mode) != null) this.mode = mode;
  }

  @Override
  public @NotNull SubsystemIntake.IntakeMode getMode() {
    return this.mode;
  }

  @Override
  public void periodic() {
    final Double targetVelocity = this.targetSpeeds.get(this.mode);

    if (targetVelocity != null) {
      if (this.drive.getLeftVelCached() == null || this.drive.getRightVelCached() == null) {
        throw new UnsupportedOperationException(Util.getLogPrefix(this) + "Can't use drive without encoders.");
      }

      // TODO The right motor's output is reversed in drive currently, so we subtract its velocity.
      final double driveVelocity = (this.drive.getLeftVelCached() - this.drive.getRightVelCached()) * 0.5;
      final double adjustedIntakeVelocity = targetVelocity - driveVelocity;

      final double minVelocity = targetVelocity * this.minVelFraction;
      final double finalVelocity = Math.max(minVelocity, adjustedIntakeVelocity);

      // TODO Make the units right
      this.motor.setVelocityUPS(finalVelocity);
    }
  }

  /**
   * Set output to a given input.
   *
   * @param input The input to give to the motor.
   */
  @Override
  public void set(final double input) {
    this.motor.setVelocity(input);
  }

  /**
   * Disable the motor.
   */
  @Override
  public void disable() {
    this.motor.disable();
  }
}
