package org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner.ControlPanelColor;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class _2020SetControlPanelPositionControlColor extends InstantCommand {
  /**
   * Default constructor.
   */
  @JsonCreator
  public _2020SetControlPanelPositionControlColor(@NotNull final _2020ControlPanelSpinner spinner,
                                                  @NotNull final ControlPanelColor color) {
    super(() -> spinner.setTargetColor(color));
  }
}
