package org.usfirst.frc.team449.robot.generalInterfaces;

import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.jacksonWrappers.PDP;

public interface SmartMotorSlave {
     void setMaster(int port,
                          boolean brakeMode,
                          @Nullable Integer currentLimit,
                          @Nullable Integer voltageCompSamples,
                          @Nullable PDP PDP);
}
