package org.usfirst.frc.team449.robot._2020.intake;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

import java.util.Map;

import static org.usfirst.frc.team449.robot.other.Util.getLogPrefix;

/**
 * A simple two-sided intake subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeTwoSidesSimple extends SubsystemBase implements Loggable, SubsystemIntake {

  /**
   * The motors this subsystem controls.
   */
  @NotNull
  private final SimpleMotor leftMotor, rightMotor;

  /**
   * The velocities for the motor to go at for each of the modes, on [-1, 1]. Can be null to
   * indicate that this intake doesn't have/use that mode.
   */
  @NotNull
  private final Map<IntakeMode, Double> velocities;

  /**
   * The current mode.
   */
  @NotNull
  private IntakeMode mode;

  /**
   * Default constructor
   *
   * @param leftMotor The left motor that this subsystem controls.
   * @param rightMotor The left motor that this subsystem controls.
   * @param velocities The velocity for the motor to go at for each {@link IntakeMode}, on the
   * interval [-1, 1]. Modes can be missing to indicate that this intake doesn't have/use them.
   */
  @JsonCreator
  public IntakeTwoSidesSimple(@JsonProperty(required = true) @NotNull final SimpleMotor leftMotor,
                              @JsonProperty(required = true) @NotNull final SimpleMotor rightMotor,
                              @NotNull @JsonProperty(required = true) final Map<IntakeMode, Double> velocities) {

    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
    this.mode = IntakeMode.OFF;
    this.velocities = velocities;

    if (velocities.containsKey(IntakeMode.OFF))
      System.err.println(getLogPrefix(this) + "Warning: velocity for mode " + IntakeMode.OFF + " will be ignored.");

    if (velocities.isEmpty()) {
      System.err.println(
          getLogPrefix(this) + "Warning: no defined velocities; motor will never spin.");
    }
  }

  /**
   * @return the current mode of the intake.
   */
  @Override
  @NotNull

  @Log.ToString
  public IntakeMode getMode() {
    return this.mode;
  }

  /**
   * @param mode The mode to switch the intake to.
   */
  @Override
  public void setMode(@NotNull final IntakeMode mode) {
    this.setLeftMode(mode);
    this.setRightMode(mode);
  }

  /**
   * @param mode The mode to switch the left side of the intake to.
   */

  public void setLeftMode(@NotNull final IntakeMode mode) {
    this.setMode(mode, this.leftMotor);
  }

  /**
   * @param mode The mode to switch the right side of the intake to.
   */

  public void setRightMode(@NotNull final IntakeMode mode) {
    this.setMode(mode, this.rightMotor);
  }

  /**
   * @param mode
   * @param motor
   */
  public void setMode(@NotNull final IntakeMode mode, @NotNull final SimpleMotor motor) {
    if (mode == IntakeMode.OFF) {
      motor.setVelocity(0);
      motor.disable();
    } else if (this.velocities.containsKey(mode)) {
      motor.enable();
      motor.setVelocity(this.velocities.get(mode));
      this.mode = mode;
    } else {
      System.err.println(getLogPrefix(this) + "Warning: use of undefined mode " + mode);
    }
  }
}
