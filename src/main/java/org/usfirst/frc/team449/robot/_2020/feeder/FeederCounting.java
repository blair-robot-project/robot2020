package org.usfirst.frc.team449.robot._2020.feeder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.usfirst.frc.team449.robot.generalInterfaces.SmartMotor;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;

import java.util.Map;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FeederCounting extends SubsystemBase implements Loggable {

    private SmartMotor transitionMotor;
    private SmartMotor mainMotor;

    private MappedDigitalInput sensor1;
    private MappedDigitalInput sensor2;

    @Log
    private FeederState currentState;

    private Map<FeederState, Double> transitionVelocities;
    private Map<FeederState, Double> mainVelocities;

    int ballCount = 0;

    public enum FeederState {
        NEUTRAL, INTAKEOVERRIDE, INTAKECOMPLETE, AWAITINGFIFTH, HOLDINGFIVE, PRESHOOTING, SHOOTING
    }

    @JsonCreator
    public FeederCounting(@JsonProperty(required = true) MappedDigitalInput sensor1,
                          @JsonProperty(required = true) MappedDigitalInput sensor2,
                          @JsonProperty(required = true) SmartMotor transitionMotor,
                          @JsonProperty(required = true) SmartMotor mainMotor,
                          @JsonProperty(required = true) Map<FeederState, Double> transitionVelocities,
                          @JsonProperty(required = true) Map<FeederState, Double> mainVelocities){
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.transitionMotor = transitionMotor;
        this.mainMotor = mainMotor;

        this.transitionVelocities = transitionVelocities;
        this.mainVelocities = mainVelocities;

        currentState = FeederState.NEUTRAL;
    }

    @Override
    public void periodic(){
        switch(currentState){
            case NEUTRAL:
            case AWAITINGFIFTH:
            case HOLDINGFIVE:
                transitionMotor.setVelocity(0);
                mainMotor.setVelocity(0);
                break;
            case INTAKEOVERRIDE:
                transitionMotor.setVelocity(transitionVelocities.get(currentState));
                mainMotor.setVelocity(0);
                break;
            default:
                transitionMotor.setVelocity(transitionVelocities.get(currentState));
                mainMotor.setVelocity(mainVelocities.get(currentState));
        }
    }

    public void setState(FeederState state){
        currentState = state;
    }

    public FeederState getState(){
        return currentState;
    }

    public boolean sensorsTripped(){
        return sensor1.get() && sensor2.get();
    }


}
