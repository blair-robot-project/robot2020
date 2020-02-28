package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.NotNull;

/**
 * A roboRIO digital input pin. Caches the input, only updating when {@link
 * MappedDigitalInput#periodic()} is called.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public final class MappedDigitalInput extends DigitalInput implements Subsystem, Loggable, BooleanSupplier {

  private boolean value;

  /**
   * Create an instance of a Digital Input class. Creates a digital input given a channel.
   *
   * @param channel the DIO channel for the digital input 0-9 are on-board, 10-25 are on the MXP
   */
  @JsonCreator
  public MappedDigitalInput(@JsonProperty(required = true) final int channel) {
    super(channel);
    this.register();
  }

  /**
   * Get the value from a digital input channel. Retrieve the value of a single digital input
   * channel from the FPGA.
   *
   * @return the status of the digital input
   */
  @Override
  @Log
  public boolean get() {
    return this.value;
  }

  /**
   * Returns the result of {@link MappedDigitalInput#get()}.
   *
   * @return the return value of {@link MappedDigitalInput#get()}
   */
  @Override
  public boolean getAsBoolean() {
    return this.get();
  }

  /**
   * This method is called periodically by the {@link CommandScheduler}.  Useful for updating
   * subsystem-specific state that you don't want to offload to a {@link Command}.  Teams should try
   * to be consistent within their own codebases about which responsibilities will be handled by
   * Commands, and which will be handled here.
   */
  @Override
  public void periodic() {
    this.value = !super.get(); // true is off by default in WPILib, and that's dumb
  }

  /**
   * Get the name of this object.
   *
   * @return A string that will identify this object in the log file.
   */
  @NotNull
  @Override
  public String configureLogName() {
    return this.getClass().getSimpleName() + this.getChannel();
  }
}
