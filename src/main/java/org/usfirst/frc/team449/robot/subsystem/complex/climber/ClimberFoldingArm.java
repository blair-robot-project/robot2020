package org.usfirst.frc.team449.robot.subsystem.complex.climber;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.SubsystemBinaryMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.climber.SubsystemClimberFoldingArm;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.SubsystemSolenoid;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ClimberFoldingArm extends SubsystemBase implements SubsystemClimberFoldingArm, SubsystemBinaryMotor, SubsystemSolenoid, Loggable {

    @NotNull
    private final ClimberCurrentLimited motorSubsystem;

    @NotNull
    private final SubsystemSolenoid solenoidSubsystem;

    /**
     * Default constructor.
     *
     * @param motorSubsystem    The motor used to winch when climbing.
     * @param solenoidSubsystem The piston used to raise the arm.
     */
    @JsonCreator
    public ClimberFoldingArm(@NotNull @JsonProperty(required = true) ClimberCurrentLimited motorSubsystem,
                             @NotNull @JsonProperty(required = true) SubsystemSolenoid solenoidSubsystem) {
        this.motorSubsystem = motorSubsystem;
        this.solenoidSubsystem = solenoidSubsystem;
    }

    @Override
    public void raise() {
        this.turnMotorOff();
        this.setSolenoid(DoubleSolenoid.Value.kForward);
    }

    @Override
    public void lower() {
        this.setSolenoid(DoubleSolenoid.Value.kOff);
        this.turnMotorOn();
    }

    @Override
    public void off() {
        this.turnMotorOff();
        this.setSolenoid(DoubleSolenoid.Value.kOff);
    }

    /**
     * Turns the motor on, and sets it to a map-specified speed.
     */
    @Override
    public void turnMotorOn() {
        this.motorSubsystem.turnMotorOn();
    }

    /**
     * Turns the motor off.
     */
    @Override
    public void turnMotorOff() {
        this.motorSubsystem.turnMotorOff();
    }

    /**
     * @return true if the motor is on, false otherwise.
     */
    @Override
    public boolean isMotorOn() {
        return this.motorSubsystem.isMotorOn();
    }

    /**
     * @param value The position to set the solenoid to.
     */
    @Override
    public void setSolenoid(DoubleSolenoid.@NotNull Value value) {
        this.solenoidSubsystem.setSolenoid(value);
    }

    /**
     * @return the current position of the solenoid.
     */
    @Override
    public @NotNull DoubleSolenoid.Value getSolenoidPosition() {
        return this.solenoidSubsystem.getSolenoidPosition();
    }
}
