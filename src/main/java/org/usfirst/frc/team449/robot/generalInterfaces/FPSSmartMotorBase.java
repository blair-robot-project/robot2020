package org.usfirst.frc.team449.robot.generalInterfaces;

import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link FPSSmartMotor} to add {@link Log} to certain methods.
 */
public abstract class FPSSmartMotorBase implements FPSSmartMotor {
    @Log
    @Override
    public abstract String getControlMode();

    @Log
    @Override
    public abstract double getOutputVoltage();

    // TODO This causes NullPointerException
    @Override
    @Nullable
    public abstract Double getSetpoint();

    @Log
    @Override
    public boolean isSimulated() {
        return false;
    }
}
