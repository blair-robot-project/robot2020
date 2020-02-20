package org.usfirst.frc.team449.robot._2020.spinner.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;

public class ExtendAndSpin extends InstantCommand {

  @JsonCreator
  public ExtendAndSpin(@NotNull @JsonProperty(required = true) final Solenoid piston,
                       @NotNull @JsonProperty(required = true) final int numRotations) {

  }

}
