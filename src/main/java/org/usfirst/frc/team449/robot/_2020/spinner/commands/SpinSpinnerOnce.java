package org.usfirst.frc.team449.robot._2020.spinner.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;

@JsonIdentityInfo(generator = StringIdGenerator.class)
public class SpinSpinnerOnce extends InstantCommand {

  private static final double DEFAULT_TOLERANCE = .5;

  /**
   * @param motor The motor to spin
   * @param inches The distance to spin
   * @param tolerance How much it can bear to be off, in distance inches
   */
  @JsonCreator
  public static SpinSpinnerOnce create(
      @NotNull @JsonProperty(required = true) SmartMotor motor,
      @JsonProperty(required = true) double inches,
      @Nullable Double tolerance) {
    var tol = tolerance == null ? DEFAULT_TOLERANCE : tolerance;
    return new SpinSpinnerOnce(() -> {
      var setpoint = motor.getSetpoint();
      motor.setPositionSetpoint(setpoint == null ? 0 : setpoint + inches / 12);
    });
  }

  public SpinSpinnerOnce(@NotNull Runnable toRun) {
    super(toRun);
  }
}
