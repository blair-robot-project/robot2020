package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.FlywheelSimple;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SubsystemFlywheel;
import org.usfirst.frc.team449.robot.components.MapInterpolationComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
public class DualFlywheel extends SubsystemBase {

    FlywheelSimple leftWheel;
    FlywheelSimple rightWheel;
    SimpleMotor kicker;

    DualFlywheelState currentState;

    double kickerSpeed;

    public DualFlywheel(@JsonProperty(required = true) FlywheelSimple leftWheel,
                        @JsonProperty(required = true) FlywheelSimple rightWheel,
                        @JsonProperty(required = true) SimpleMotor kicker,
                        double kickerSpeed,
                        MapInterpolationComponent interpolator,
                        Double LeftRightSpeedDifference){
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        this.kicker = kicker;

        currentState = DualFlywheelState.NEUTRAL;

        this.kickerSpeed = kickerSpeed;
    }

    @Override
    public void periodic(){
        if(currentState == DualFlywheelState.NEUTRAL){
            leftWheel.turnFlywheelOff();
            rightWheel.turnFlywheelOff();
            kicker.setVelocity(0);
        }
        if(currentState == DualFlywheelState.SPINNINGUP){
            leftWheel.turnFlywheelOn();
            rightWheel.turnFlywheelOn();
        }

        if(leftWheel.isReadyToShoot() && rightWheel.isReadyToShoot()){
            currentState = DualFlywheelState.SHOOTING;
            kicker.setVelocity(kickerSpeed);
        }
    }

    public enum DualFlywheelState{
        NEUTRAL, SPINNINGUP, SHOOTING
    }
}
