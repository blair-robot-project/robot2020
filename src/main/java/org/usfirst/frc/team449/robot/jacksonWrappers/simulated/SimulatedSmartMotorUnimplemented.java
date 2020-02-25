package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.components.RunningLinRegComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;
import org.usfirst.frc.team449.robot.generalInterfaces.shiftable.Shiftable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveSparkMax;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.SlaveVictor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class that implements {@link SmartMotor} without relying on the existence of actual hardware.
 * This class performs no simulation; all of its methods return zero values.
 * <p>
 * This class is automatically instantiated by the FPSSmartMotor factory method when the robot is
 * running in a simulation and should not be otherwise referenced in code.
 * </p>
 */
public class SimulatedSmartMotorUnimplemented implements SmartMotor, Loggable {
  @NotNull
  @Log
  private final String name;
  @NotNull
  @Log.ToString
  private final Type controllerType;
  @Log
  private final int port;
  private final boolean reverseOutput;
  private final double feetPerRotation;

  public SimulatedSmartMotorUnimplemented(final Type type,
                                          final int port,
                                          final boolean enableBrakeMode,
                                          @Nullable final String name,
                                          final boolean reverseOutput,
                                          @Nullable final PDP PDP,
                                          @Nullable final Boolean fwdLimitSwitchNormallyOpen,
                                          @Nullable final Boolean revLimitSwitchNormallyOpen,
                                          @Nullable final Integer remoteLimitSwitchID,
                                          @Nullable final Double fwdSoftLimit,
                                          @Nullable final Double revSoftLimit,
                                          @Nullable final Double postEncoderGearing,
                                          @Nullable final Double feetPerRotation,
                                          @Nullable final Integer currentLimit,
                                          final boolean enableVoltageComp,
                                          @Nullable final List<PerGearSettings> perGearSettings,
                                          @Nullable final Shiftable.gear startingGear,
                                          @Nullable final Integer startingGearNum,
                                          // Spark-specific
                                          @Nullable final HashMap<CANSparkMaxLowLevel.PeriodicFrame, Integer> sparkStatusFramesMap,
                                          @Nullable final Integer controlFrameRateMillis,
                                          // Talon-specific
                                          @Nullable final HashMap<StatusFrameEnhanced, Integer> talonStatusFramesMap,
                                          @Nullable final Map<ControlFrame, Integer> controlFrameRatesMillis,
                                          @Nullable final RunningLinRegComponent voltagePerCurrentLinReg,
                                          @Nullable final Integer voltageCompSamples,
                                          @Nullable final FeedbackDevice feedbackDevice,
                                          @Nullable final Integer encoderCPR,
                                          @Nullable final Boolean reverseSensor,
                                          @Nullable final Double updaterProcessPeriodSecs,
                                          @Nullable final List<SlaveTalon> slaveTalons,
                                          @Nullable final List<SlaveVictor> slaveVictors,
                                          @Nullable final List<SlaveSparkMax> slaveSparks) {
    this.controllerType = type;
    this.port = port;
    this.reverseOutput = reverseOutput;
    this.feetPerRotation = Objects.requireNonNullElse(feetPerRotation, 1.0);
    this.name = name != null ? name : String.format("%s_%d", type == Type.SPARK ? "spark" : type == Type.TALON ? "talon" : "MotorControllerUnknownType", port);

  }

  /**
   * Set the motor output voltage to a given percent of available voltage.
   *
   * @param percentVoltage percent of total voltage from [-1, 1]
   */
  @Override
  public void setPercentVoltage(final double percentVoltage) {
  }

  /**
   * Convert from native units read by an encoder to feet moved. Note this DOES account for
   * post-encoder gearing.
   *
   * @param nativeUnits A distance native units as measured by the encoder.
   * @return That distance in feet, or null if no encoder CPR was given.
   */
  @Override
  public double encoderToUnit(final double nativeUnits) {
    return nativeUnits;
  }

  /**
   * Convert a distance from feet to encoder reading in native units. Note this DOES account for
   * post-encoder gearing.
   *
   * @param feet A distance in feet.
   * @return That distance in native units as measured by the encoder, or null if no encoder CPR was
   * given.
   */
  @Override
  public double unitToEncoder(final double feet) {
    return feet;
  }

  /**
   * Converts the velocity read by the controllers's getVelocity() method to the FPS of the output
   * shaft. Note this DOES account for post-encoder gearing.
   *
   * @param encoderReading The velocity read from the encoder with no conversions.
   * @return The velocity of the output shaft, in FPS, when the encoder has that reading, or null if
   * no encoder CPR was given.
   */
  @Override
  public double encoderToUPS(final double encoderReading) {
    return encoderReading;
  }

  /**
   * Converts from the velocity of the output shaft to what the controllers's getVelocity() method
   * would read at that velocity. Note this DOES account for post-encoder gearing.
   *
   * @param FPS The velocity of the output shaft, in FPS.
   * @return What the raw encoder reading would be at that velocity, or null if no encoder CPR was
   * given.
   */
  @Override
  public double UPSToEncoder(final double FPS) {
    return FPS;
  }

  /**
   * Convert from native velocity units to output rotations per second. Note this DOES NOT account
   * for post-encoder gearing.
   *
   * @param nat A velocity in native units.
   * @return That velocity in RPS, or null if no encoder CPR was given.
   */
  @Override
  public Double nativeToRPS(final double nat) {
    return nat;
  }

  /**
   * Convert from output RPS to the native velocity. Note this DOES NOT account for post-encoder
   * gearing.
   *
   * @param RPS The RPS velocity you want to convert.
   * @return That velocity in native units, or null if no encoder CPR was given.
   */
  @Override
  public double RPSToNative(final double RPS) {
    return RPS;
  }

  /**
   * @return Raw position units for debugging purposes
   */
  @Override
  @Log
  public double encoderPosition() {
    return 0;
  }

  /**
   * Set a position setpoint for the controller.
   */
  @Override
  public void setPositionSetpoint(final double feet) {
  }

  /**
   * @return Raw velocity units for debugging purposes
   */
  @Override
  @Log
  public double encoderVelocity() {
    return 0;
  }

  /**
   * Sets the output in volts.
   *
   * @param volts
   */
  @Override
  public void setVoltage(final double volts) {

  }

  /**
   * Get the velocity of the controller in FPS.
   *
   * @return The controller's velocity in FPS, or null if no encoder CPR was given.
   */
  @Override
  public Double getVelocity() {
    return null;
  }

  /**
   * Set the velocity for the motor to go at.
   *
   * @param velocity the desired velocity, on [-1, 1].
   */
  @Override
  public void setVelocity(final double velocity) {
  }

  /**
   * Give a velocity closed loop setpoint in FPS.
   *
   * @param velocity velocity setpoint in FPS.
   */
  @Override
  public void setVelocityUPS(final double velocity) {

  }

  /**
   * Enables the motor, if applicable.
   */
  @Override
  public void enable() {
  }

  /**
   * Disables the motor, if applicable.
   */
  @Override
  public void disable() {
  }

  /**
   * Get the current closed-loop velocity error in FPS. WARNING: will give garbage if not in
   * velocity mode.
   *
   * @return The closed-loop error in FPS, or null if no encoder CPR was given.
   */
  @Override
  public double getError() {
    return 0;
  }

  /**
   * Get the current velocity setpoint of the Talon in FPS, the position setpoint in feet
   *
   * @return The setpoint in sensible units for the current control mode.
   */
  @Override
  @Nullable
  public Double getSetpoint() {
    return null;
  }

  /**
   * Get the voltage the Talon is currently drawing from the PDP.
   *
   * @return Voltage in volts.
   */
  @Override
  @Log
  public double getOutputVoltage() {
    return 0;
  }

  /**
   * Get the voltage available for the Talon.
   *
   * @return Voltage in volts.
   */
  @Override
  @Log
  public double getBatteryVoltage() {
    return 0;
  }

  /**
   * Get the current the Talon is currently drawing from the PDP.
   *
   * @return Current in amps.
   */
  @Override
  @Log
  public double getOutputCurrent() {
    return 0;
  }

  /**
   * Get the current control mode of the Talon. Please don't use this for anything other than
   * logging.
   *
   * @return Control mode as a string.
   */
  @NotNull
  @Override
  public String getControlMode() {
    return "Unimplemented";
  }

  /**
   * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
   *
   * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given
   * gear.
   * @param gear The number of the gear to use the max speed from to scale the velocity.
   */

  @Override
  public void setGearScaledVelocity(final double velocity, final int gear) {
  }

  /**
   * Set the velocity scaled to a given gear's max velocity. Used mostly when autoshifting.
   *
   * @param velocity The velocity to go at, from [-1, 1], where 1 is the max speed of the given
   * gear.
   * @param gear The gear to use the max speed from to scale the velocity.
   */
  @Override
  public void setGearScaledVelocity(final double velocity, final gear gear) {
  }

  /**
   * @return Feedforward calculator for this gear
   */
  @Override
  public SimpleMotorFeedforward getCurrentGearFeedForward() {
    return null;
  }

  /**
   * @return the position of the talon in feet, or null of inches per rotation wasn't given.
   */
  @Override
  public Double getPositionUnits() {
    return null;
  }

  /**
   * Resets the position of the Talon to 0.
   */
  @Override
  public void resetPosition() {
  }

  /**
   * Get the status of the forwards limit switch.
   *
   * @return True if the forwards limit switch is closed, false if it's open or doesn't exist.
   */
  @Override
  public boolean getFwdLimitSwitch() {
    return false;
  }

  /**
   * Get the status of the reverse limit switch.
   *
   * @return True if the reverse limit switch is closed, false if it's open or doesn't exist.
   */
  @Override
  public boolean getRevLimitSwitch() {
    return false;
  }

  @Override
  public boolean isInhibitedForward() {
    return false;
  }

  @Override
  public boolean isInhibitedReverse() {
    return false;
  }

  @Override
  public int getPort() {
    return this.port;
  }

  /**
   * @return The gear this subsystem is currently in.
   */
  @Override
  public int getGear() {
    return 0;
  }

  /**
   * Shift to a specific gear.
   *
   * @param gear Which gear to shift to.
   */
  @Override
  public void setGear(final int gear) {
  }

  @Override
  public String configureLogName() {
    return this.name;
  }

  @Override
  public boolean isSimulated() {
    return true;
  }
}
