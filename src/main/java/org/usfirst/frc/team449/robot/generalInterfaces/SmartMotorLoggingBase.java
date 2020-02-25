package org.usfirst.frc.team449.robot.generalInterfaces;

import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;

/**
 * B ase class for {@link SmartMotor} that adds {@link Log} to certain methods.
 */
public abstract class SmartMotorLoggingBase implements SmartMotor {
  @Log
  @NotNull
  @Override
  public abstract String getControlMode();

  @Log
  @Override
  public abstract double getOutputVoltage();

  @Log
  @Override
  public boolean isSimulated() {
    return false;
  }

  @Log
  @NotNull
  private String log_getSetpoint() {
    return String.valueOf(this.getSetpoint());
  }
}
