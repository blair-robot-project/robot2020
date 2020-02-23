package org.usfirst.frc.team449.robot._2020.intake;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot._2020.climber.SubsystemSolenoid;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemIntake;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDoubleSolenoid;

import java.util.Map;

/**
 * An intake that goes up and down with a piston.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeActuatedTwoSides extends IntakeTwoSidesSimple implements Subsystem, SubsystemSolenoid, SubsystemIntake {

  /**
   * The piston for actuating the intake.
   */
  @NotNull
  private final DoubleSolenoid piston;
  /**
   * The current position of the piston
   */
  @NotNull
  private DoubleSolenoid.Value currentPistonPos;

  /**
   * Default constructor.
   *
   * @param piston The piston for actuating the intake.
   * @param leftMotor The left motor that this subsystem controls.
   * @param rightMotor The left motor that this subsystem controls.
   * @param velocities The velocity for the motor to go at for each {@link IntakeMode}, on the
   * interval [-1, 1]. Modes can be missing to indicate that this intake doesn't have/use them.
   */
  @JsonCreator
  public IntakeActuatedTwoSides(@NotNull @JsonProperty(required = true) final MappedDoubleSolenoid piston,
                                @NotNull @JsonProperty(required = true) final SimpleMotor leftMotor,
                                @NotNull @JsonProperty(required = true) final SimpleMotor rightMotor,
                                @NotNull @JsonProperty(required = true) final Map<IntakeMode, Double> velocities) {
    super(leftMotor, rightMotor, velocities);
    this.currentPistonPos = DoubleSolenoid.Value.kOff;
    this.piston = piston;
  }

  /**
   * @param value The position to set the solenoid to.
   */
  @Override
  public void setSolenoid(@NotNull final DoubleSolenoid.Value value) {
    this.currentPistonPos = value;
    this.piston.set(value);
  }

  /**
   * @return the current position of the solenoid.
   */
  @Override
  @NotNull
  public DoubleSolenoid.Value getSolenoidPosition() {
    return this.currentPistonPos;
  }
}
