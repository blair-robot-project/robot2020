package org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.colorSpinner._2020ControlPanelSpinner.ControlPanelColor;

public class _2020SetControlPanelPositionControlColor extends InstantCommand {
    /**
     * Default constructor.
     */
    public _2020SetControlPanelPositionControlColor(@NotNull final _2020ControlPanelSpinner spinner,
                                                    @NotNull final ControlPanelColor color) {
        super(() -> spinner.setTargetColor(color));
    }
}
