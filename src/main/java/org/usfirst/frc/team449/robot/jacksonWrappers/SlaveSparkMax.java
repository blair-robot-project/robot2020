package org.usfirst.frc.team449.robot.jacksonWrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Nullable;

public class SlaveSparkMax implements Loggable {

    CANSparkMax slaveSpark;

    PDP PDP;

    @JsonCreator
    public SlaveSparkMax(@JsonProperty(required = true) int port,
                         @Nullable Boolean invert) {

        slaveSpark = new CANSparkMax(port, CANSparkMaxLowLevel.MotorType.kBrushless);

        if(invert != null){
            slaveSpark.setInverted(invert);
        }

        slaveSpark.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
        slaveSpark.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);

        slaveSpark.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 100);
        slaveSpark.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 100);
        slaveSpark.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 100);
    }

    public void setMaster(int masterPort, boolean brakeMode, @Nullable PDP PDP) {
        slaveSpark.follow(CANSparkMax.ExternalFollower.kFollowerSparkMax, masterPort);
        slaveSpark.setIdleMode(brakeMode ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
        this.PDP = PDP;
    }

    @Log
    public double getOutputCurrent (){
        return  slaveSpark.getOutputCurrent();
    }

    @Log
    public double getMotorOutputVoltage(){
        return slaveSpark.getAppliedOutput();
    }
}
