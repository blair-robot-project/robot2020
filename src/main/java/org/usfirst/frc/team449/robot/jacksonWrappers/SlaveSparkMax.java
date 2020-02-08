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

    boolean inverted;

    @JsonCreator
    public SlaveSparkMax(@JsonProperty(required = true) int port,
                         @Nullable Boolean invert,
                         @Nullable PDP PDP) {

        slaveSpark = new CANSparkMax(port, CANSparkMaxLowLevel.MotorType.kBrushless);

        inverted = invert == null ? false : invert;

        slaveSpark.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
        slaveSpark.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);

        slaveSpark.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 100);
        slaveSpark.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 100);
        slaveSpark.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 100);

        this.PDP = PDP;
    }

    public void setMasterSpark(CANSparkMax masterController, boolean brakeMode) {
        slaveSpark.follow(masterController, inverted);
        slaveSpark.setIdleMode(brakeMode ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
    }

    public void setMasterPhoenix(int masterPort, boolean brakeMode){
        slaveSpark.follow(CANSparkMax.ExternalFollower.kFollowerPhoenix, masterPort);
        slaveSpark.setIdleMode(brakeMode ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
        slaveSpark.setInverted(inverted);
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
