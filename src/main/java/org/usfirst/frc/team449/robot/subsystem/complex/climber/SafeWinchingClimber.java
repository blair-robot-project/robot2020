package org.usfirst.frc.team449.robot.subsystem.complex.climber;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.SubsystemBinaryMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.climber.SubsystemClimberWithArm;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.SubsystemSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SafeWinchingClimber extends SubsystemBase implements SubsystemClimberWithArm, SubsystemBinaryMotor, SubsystemSolenoid, Loggable {
    private final ClimberCurrentLimited motorSubsystem;
    private final SubsystemSolenoid solenoidSubsystem;
    private final long extensionTimeMillis;
    private boolean enableArm = true;

    private boolean reallySure = false;
    private boolean armIsExtending = false;
    private long extensionStartTime = 0L;

    @JsonCreator
    public SafeWinchingClimber(@NotNull @JsonProperty(required = true) final ClimberCurrentLimited motorSubsystem,
                               @NotNull @JsonProperty(required = true) final SubsystemSolenoid solenoidSubsystem,
                               final long extensionTimeMillis) {
        this.motorSubsystem = motorSubsystem;
        this.solenoidSubsystem = solenoidSubsystem;
        this.extensionTimeMillis = extensionTimeMillis;
    }

    @Override
    public void raise() {
        System.out.println("[" + this.getClass().getName() + "] raise");

        if (enableArm) {
            this.setSolenoid(DoubleSolenoid.Value.kForward);
            extensionStartTime = Clock.currentTimeMillis();
        }
    }

    @Override
    public void lower() {
        System.out.println("[" + this.getClass().getName() + "] lower");

        if (enableArm) {
            setSolenoid(DoubleSolenoid.Value.kReverse);
        }
    }

    @Override
    public void off() {
        System.out.println("[" + this.getClass().getName() + "] off");

        setSolenoid(DoubleSolenoid.Value.kOff);
        turnMotorOff();
    }

    @Override
    public void setSolenoid(@NotNull final DoubleSolenoid.Value value) {
        solenoidSubsystem.setSolenoid(value);
        armIsExtending = value == DoubleSolenoid.Value.kForward;
        reallySure = false;
    }

    @Override
    public @NotNull DoubleSolenoid.Value getSolenoidPosition() {
        return solenoidSubsystem.getSolenoidPosition();
    }

    @Override
    public void turnMotorOn() {
        if (armIsUp()) {
            if (!reallySure) {
                reallySure = true;
            } else {
                setSolenoid(DoubleSolenoid.Value.kOff);
                motorSubsystem.turnMotorOn();
                enableArm = false;
            }
        }
    }

    @Log
    public boolean armIsUp() {
        if (!armIsExtending) {
            return false;
        }
        return Clock.currentTimeMillis() >= extensionStartTime + extensionTimeMillis;
    }

    @Override
    public void turnMotorOff() {
        motorSubsystem.turnMotorOff();
        reallySure = false;
    }

    @Override
    @Log
    public boolean isMotorOn() {
        return motorSubsystem.isMotorOn();
    }
}
