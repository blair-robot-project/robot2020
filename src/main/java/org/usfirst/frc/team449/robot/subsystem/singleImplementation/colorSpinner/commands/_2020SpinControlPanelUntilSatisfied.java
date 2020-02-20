package org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner.commands;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.commands.general.WaitUntilConditionCommand;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner.ControlPanelColor;

/**
 * Runs
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class _2020SpinControlPanelUntilSatisfied extends WaitUntilConditionCommand {
  @NotNull
  private final _2020ControlPanelSpinner spinner;
  private final double speed;

  /**
   * Default constructor.
   *
   * @param spinner The subsystem to execute this command on.
   * @param speed The setpoint at which to run the spinner motor.
   */
  public _2020SpinControlPanelUntilSatisfied(@NotNull final _2020ControlPanelSpinner spinner,
                                             final double speed) {
    super(spinner::isConditionTrueCached);

    this.spinner = spinner;
    this.speed = speed;
  }

  @Override
  public void execute() {
    final ControlPanelColor target = this.spinner.getTargetColor();
    final ControlPanelColor current = this.spinner.get();

    if (current == null) {
      // Try to move the color wheel so it might get to a position where a color can be read.
      this.spinner.getMotor().set(speed);
    } else if (target != null) {
      this.spinner.getMotor().set(getOptimalSpinDirection(current, target) ? speed : -speed);
    }
  }

  @Override
  public void end(final boolean interrupted) {
    this.spinner.getMotor().disable();
  }

  /**
   * Gets the direction in which the control panel should be spun in order to go from a sector of
   * one specified color to a sector of another specified color.
   *
   * @param current the color to start at
   * @param target the color to go to
   * @return {@code true} for clockwise/left or {@code false} for counterclockwise/right
   *
   * @implNote returns {@code true} when both directions are equally optimal or when {@code current}
   * and {@code target} are the same color.
   */
  public static boolean getOptimalSpinDirection(@NotNull final ControlPanelColor current, @NotNull final ControlPanelColor target) {
    // We are evidently programmers of sophistication for our usage of Enum.ordinal().
    int diff = target.ordinal() - current.ordinal();
    if (diff < 0) diff += ControlPanelColor.values().length;
    return diff * 2 <= ControlPanelColor.values().length;
  }

}
