package org.usfirst.frc.team449.robot._2020.feeder;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.other.Debouncer;

import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FeederCounting extends SubsystemBase implements Loggable {

    private SmartMotor transitionMotor;
    private SmartMotor mainMotor;

    private MappedDigitalInput ballSensor;

    @Log.ToString
    private FeederState currentState;

    private Map<FeederState, Double> transitionVelocities;
    private Map<FeederState, Double> mainVelocities;

    private double preShootingReverseDelay = 0;

    @Log
    private int ballCount = 0;
    private boolean ballWasPresent;
    private Debouncer sensorDebouncer;

    @JsonCreator
    public FeederCounting(@JsonProperty(required = true) MappedDigitalInput ballSensor,
                          @JsonProperty(required = true) SmartMotor transitionMotor,
                          @JsonProperty(required = true) SmartMotor mainMotor,
                          @JsonProperty(required = true) Map<FeederState, Double> transitionVelocities,
                          @JsonProperty(required = true) Map<FeederState, Double> mainVelocities,
                          Double ballSensorDebounce,
                          Double preShootingReverseDelay) {
        this.ballSensor = ballSensor;
        this.transitionMotor = transitionMotor;
        this.mainMotor = mainMotor;

        this.transitionVelocities = transitionVelocities;
        this.mainVelocities = mainVelocities;

        this.preShootingReverseDelay = preShootingReverseDelay == null ? /*hardcoded val*/ 0.1 : preShootingReverseDelay;

        sensorDebouncer = new Debouncer(ballSensorDebounce == null ? /*hardcoded val*/0.2 : ballSensorDebounce);

        currentState = FeederState.NEUTRAL;
    }

    @Override
    public void periodic() {
        updateBallCount();
        switch (currentState) {
            case NEUTRAL:
            case HOLDINGFIVE:
                transitionMotor.setVelocity(0);
                mainMotor.setVelocity(0);
                break;
            case INTAKEOVERRIDE:
                transitionMotor.setVelocity(transitionVelocities.get(currentState));
                mainMotor.setVelocity(0);
                break;
            case SHOOTING:
                resetBallCount();
                transitionMotor.setVelocity(0);
                mainMotor.setVelocity(mainVelocities.get(currentState));
            default:
                transitionMotor.setVelocity(transitionVelocities.get(currentState));
                mainMotor.setVelocity(mainVelocities.get(currentState));
        }
    }

    public FeederState getState() {
        return currentState;
    }

    public void setState(FeederState state) {
        currentState = state;
    }

    public boolean getBallPresent() {
        return sensorDebouncer.get(ballSensor.get());
    }

    public void updateBallCount() {
        if (ballWasPresent && !getBallPresent()) {
            ballCount++;
        }
        ballWasPresent = getBallPresent();
    }

    public int getBallCount() {
        return ballCount;
    }

    public void resetBallCount() {
        ballCount = 0;
    }

    public double getPreShootingReverseDelay() {
        return preShootingReverseDelay;
    }

    public enum FeederState {
        NEUTRAL, INTAKEOVERRIDE, INTAKECOMPLETE, HOLDINGFIVE, PRESHOOTING, SHOOTING
    }


}
