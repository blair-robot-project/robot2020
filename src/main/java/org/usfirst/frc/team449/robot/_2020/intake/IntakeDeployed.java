package org.usfirst.frc.team449.robot._2020.intake;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot._2020.multiSubsystem.SolenoidSimple;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class IntakeDeployed extends SubsystemBase {

    SimpleMotor motor;
    SolenoidSimple piston;

    double intakeSpeed;

    IntakeState currentState;

    @JsonCreator
    public IntakeDeployed(@JsonProperty(required = true) SimpleMotor motor,
                          @JsonProperty(required = true) SolenoidSimple piston,
                          @Nullable Double intakeSpeed){
        this.motor = motor;
        this.piston = piston;
        this.intakeSpeed = intakeSpeed == null ? 0.8 : intakeSpeed;

        currentState = IntakeState.NEUTRAL;
    }

    @Override
    public void periodic(){
        switch(currentState){
            case NEUTRAL:
                motor.setVelocity(0);
                piston.setSolenoid(DoubleSolenoid.Value.kReverse);
                break;
            case NEUTRALDEPLOYED:
                motor.setVelocity(0);
                piston.setSolenoid(DoubleSolenoid.Value.kForward);
                break;
            case FORWARD:
                motor.setVelocity(intakeSpeed);
                piston.setSolenoid(DoubleSolenoid.Value.kForward);
                break;
            case REVERSE:
                motor.setVelocity(-intakeSpeed);
                piston.setSolenoid(DoubleSolenoid.Value.kForward);
                break;
        }
    }

    public IntakeState getCurrentState(){
        return currentState;
    }

    public void setIntakeState(IntakeState state){
        currentState = state;
    }

    public enum IntakeState{
        NEUTRAL, NEUTRALDEPLOYED, FORWARD, REVERSE
    }

}
