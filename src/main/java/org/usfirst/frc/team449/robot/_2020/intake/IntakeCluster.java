package org.usfirst.frc.team449.robot._2020.intake;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;

/**
 * A cluster of intakes that acts as a single intake. Use for complex intakes with multiple motors.
 *
 * <p>Replaces IntakeTwoSides and friends.
 */
public class IntakeCluster implements Subsystem, SubsystemIntake {
  private final List<SubsystemIntake> intakes;

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  private IntakeCluster(@NotNull @JsonProperty(required = true) final SubsystemIntake[] intakes) {
    this.intakes = List.of(intakes);
  }

  /** @return the current mode of the first intake in this group. */
  @Override
  public @NotNull IntakeMode getMode() {
    return this.intakes.iterator().next().getMode();
  }

  /** @param mode The mode to switch the intake to. */
  @Override
  public void setMode(@NotNull final IntakeMode mode) {
    for (final SubsystemIntake intake : this.intakes) {
      intake.setMode(mode);
    }
  }

  /**
   * Returns an immutable list
   *
   * @return an immutable list of the intakes in this cluster
   */
  @NotNull
  public List<SubsystemIntake> intakes() {
    return this.intakes;
  }
}
