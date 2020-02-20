package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DoubleSolenoid;

// TODO map using mix-in

/** A Jackson-compatible wrapper on the {@link DoubleSolenoid}. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedDoubleSolenoid extends DoubleSolenoid {

  /**
   * Default constructor.
   *
   * @param module The module number of the PCM. Defaults to 0.
   * @param forward The forward port on the PCM.
   * @param reverse The reverse port on the PCM.
   */
  public MappedDoubleSolenoid(
      final int module,
      @JsonProperty(required = true) final int forward,
      @JsonProperty(required = true) final int reverse) {
    super(module, forward, reverse);
  }

  @JsonCreator
  public static MappedDoubleSolenoid create(
      final int module,
      @JsonProperty(required = true) final int forward,
      @JsonProperty(required = true) final int reverse) {
    return new MappedDoubleSolenoid(module, forward, reverse);
  }
}
