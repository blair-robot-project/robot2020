package org.usfirst.frc.team449.robot._2020.intake;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.IntakeSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SolenoidSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemSolenoid;

/**
 * A decorator to make an intake that goes up and down with a piston.
 *
 * @deprecated Use separate instances of {@link IntakeSimple} and {@link SolenoidSimple} instead.
 */
@Deprecated(forRemoval = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeActuated implements Subsystem, SubsystemIntake, SubsystemSolenoid, Loggable {
  @NotNull private final SubsystemIntake implementation;
  /** The piston for actuating the intake. */
  @NotNull private final DoubleSolenoid piston;
  /** The current position of the piston */
  @NotNull private DoubleSolenoid.Value currentPistonPos;

  /**
   * Default constructor.
   *
   * @param implementation The intake instance to wrap.
   * @param piston The piston for actuating the intake.
   */
  @JsonCreator
  public IntakeActuated(
      @NotNull @JsonProperty(required = true) final SubsystemIntake implementation,
      @NotNull @JsonProperty(required = true) final DoubleSolenoid piston) {
    this.implementation = implementation;
    this.piston = piston;
    this.currentPistonPos = DoubleSolenoid.Value.kOff;
  }

  /** @param value The position to set the solenoid to. */
  @Override
  public void setSolenoid(@NotNull final DoubleSolenoid.Value value) {
    currentPistonPos = value;
    piston.set(value);
  }

  /** @return the current position of the solenoid. */
  @Override
  @NotNull
  public DoubleSolenoid.Value getSolenoidPosition() {
    return currentPistonPos;
  }

  /** @return the current mode of the intake. */
  @Override
  public @NotNull IntakeMode getMode() {
    return this.implementation.getMode();
  }

  /** @param mode The mode to switch the intake to. */
  @Override
  public void setMode(@NotNull final IntakeMode mode) {
    this.implementation.setMode(mode);
  }
}
