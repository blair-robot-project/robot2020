package org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveVictor;

/** A simple wrapper on the {@link VictorSPX}. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class MappedVictorSPX implements SimpleMotor, Loggable {

  /** The Victor SPX this object is a wrapper on. */
  @NotNull private final VictorSPX victorSPX;

  /**
   * Default constructor.
   *
   * @param port The CAN ID of this Victor SPX.
   * @param brakeMode Whether to have the Victor brake or coast when no voltage is applied.
   * @param inverted Whether or not to invert this Victor. Defaults to false.
   * @param enableVoltageComp Whether or not to enable voltage compensation. Defaults to false.
   * @param voltageCompSamples The number of 1-millisecond samples to use for voltage compensation.
   *     Defaults to 32.
   * @param slaveVictors Any other Victor SPXs slaved to this one.
   */
  @JsonCreator
  public MappedVictorSPX(
      @JsonProperty(required = true) final int port,
      @JsonProperty(required = true) final boolean brakeMode,
      final boolean inverted,
      final boolean enableVoltageComp,
      final Double peakVoltageForward,
      final Double peakVoltageRev,
      @Nullable final Integer voltageCompSamples,
      @Nullable final List<SlaveVictor> slaveVictors) {
    victorSPX = new VictorSPX(port);
    victorSPX.setInverted(inverted);
    victorSPX.setNeutralMode(brakeMode ? NeutralMode.Brake : NeutralMode.Coast);
    victorSPX.enableVoltageCompensation(enableVoltageComp);
    victorSPX.configVoltageCompSaturation(12, 0);
    victorSPX.configVoltageMeasurementFilter(
        voltageCompSamples != null ? voltageCompSamples : 32, 0);
    victorSPX.configPeakOutputForward(peakVoltageForward != null ? peakVoltageForward / 12. : 1, 0);
    victorSPX.configPeakOutputReverse(peakVoltageRev != null ? peakVoltageRev / 12. : -1, 0);

    if (slaveVictors != null) {
      // Set up slaves.
      for (final SlaveVictor slave : slaveVictors) {
        slave.setMaster(
            victorSPX,
            brakeMode,
            enableVoltageComp ? (voltageCompSamples != null ? voltageCompSamples : 32) : null);
      }
    }
  }

  /**
   * Set the velocity for the motor to go at.
   *
   * @param velocity the desired velocity, on [-1, 1].
   */
  @Override
  public void setVelocity(final double velocity) {
    victorSPX.set(ControlMode.PercentOutput, velocity);
  }

  /** Enables the motor, if applicable. */
  @Override
  public void enable() {
    // Do nothing
  }

  /** Disables the motor, if applicable. */
  @Override
  public void disable() {
    victorSPX.set(ControlMode.Disabled, 0);
  }

  //    /**
  //     * Get the headers for the data this subsystem logs every loop.
  //     *
  //     * @return An N-length array of String labels for data, where N is the length of the
  // Object[] returned by getData().
  //     */
  //    @NotNull
  //    @Override
  //    public String[] getHeader() {
  //        return new String[]{
  //                "bus_voltage",
  //                "voltage"
  //        };
  //    }
  //
  //    /**
  //     * Get the data this subsystem logs every loop.
  //     *
  //     * @return An N-length array of Objects, where N is the number of labels given by getHeader.
  //     */
  //    @NotNull
  //    @Override
  //    public Object[] getData() {
  //        return new Object[]{
  //                victorSPX.getBusVoltage(),
  //                victorSPX.getMotorOutputVoltage()
  //        };
  //    }
  //
  //    /**
  //     * Get the name of this object.
  //     *
  //     * @return A string that will identify this object in the log file.
  //     */
  //    @Override
  //    @NotNull
  //    public String getLogName() {
  //        return "victor_" + victorSPX.getDeviceID();
  //    }

  @Log
  public double getBusVolt() {
    return victorSPX.getBusVoltage();
  }

  @Log
  public double getMotorOutPutVolt() {
    return victorSPX.getMotorOutputVoltage();
  }
}
