package org.usfirst.frc.team449.robot._2020.shooter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.FlywheelSimple;
import org.usfirst.frc.team449.robot.components.MapInterpolationComponent;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class DualFlywheel extends SubsystemBase implements Loggable {

    @Log
    FlywheelSimple leftWheel;
    @Log
    FlywheelSimple rightWheel;

    SimpleMotor kicker;

    @Log.ToString
    DualFlywheelState currentState;

    @Log
    double kickerSpeed;

    @JsonCreator
    public DualFlywheel(@JsonProperty(required = true) FlywheelSimple leftWheel,
                        @JsonProperty(required = true) FlywheelSimple rightWheel,
                        @JsonProperty(required = true) SimpleMotor kicker,
                        double kickerSpeed,
                        @Nullable MapInterpolationComponent interpolator) {
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        this.kicker = kicker;

        currentState = DualFlywheelState.NEUTRAL;

        this.kickerSpeed = kickerSpeed;
    }

    @Override
    public void periodic() {
        leftWheel.update();
        rightWheel.update();
        if (currentState == DualFlywheelState.NEUTRAL) {
            leftWheel.turnFlywheelOff();
            rightWheel.turnFlywheelOff();
            kicker.setVelocity(0);
        }

        if (currentState == DualFlywheelState.SPINNINGUP) {
            leftWheel.turnFlywheelOn();
            rightWheel.turnFlywheelOn();
        }

        if (leftWheel.isReadyToShoot() && rightWheel.isReadyToShoot()) {
            currentState = DualFlywheelState.SHOOTING;
            kicker.setVelocity(kickerSpeed);
        }
    }

    public DualFlywheelState getCurrentState() {
        return currentState;
    }

    public void setFlywheelState(DualFlywheelState state) {
        currentState = state;
    }

    public enum DualFlywheelState {
        NEUTRAL, SPINNINGUP, SHOOTING
    }
}
