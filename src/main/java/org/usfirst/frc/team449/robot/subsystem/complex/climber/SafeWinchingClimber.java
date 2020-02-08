package org.usfirst.frc.team449.robot.subsystem.complex.climber;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.subsystem.interfaces.binaryMotor.SubsystemBinaryMotor;
import org.usfirst.frc.team449.robot.subsystem.interfaces.climber.SubsystemClimberWithArm;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.SubsystemSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;

import static org.usfirst.frc.team449.robot.other.Util.getLogPrefix;

/**
 * Like {@link ClimberWinchingWithArm} with safety
 * features (stuff needs to be enabled to move)
 * @author Nathan
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class SafeWinchingClimber extends SubsystemBase
        implements SubsystemClimberWithArm, SubsystemBinaryMotor, SubsystemSolenoid, Loggable {
    private final ClimberCurrentLimited motorSubsystem;
    private final SubsystemSolenoid solenoidSubsystem;

    private final long extensionTimeMillis;
    @Log
    private boolean armIsExtending = false;
    @Log
    private long extensionStartTime = 0L;
    @Log
    private boolean enableArm = true;
    @Log
    private boolean reallySure = false;

    @JsonCreator
    public SafeWinchingClimber(@NotNull @JsonProperty(required = true) final ClimberCurrentLimited motorSubsystem,
                               @NotNull @JsonProperty(required = true) final SubsystemSolenoid solenoidSubsystem,
                               final long extensionTimeMillis) {
        this.motorSubsystem = motorSubsystem;
        this.solenoidSubsystem = solenoidSubsystem;
        this.extensionTimeMillis = extensionTimeMillis;
    }

    /**
     * Raise arm only if it is enabled
     */
    @Override
    public void raise() {
        System.out.println(getLogPrefix(this) + "raise");

        if (enableArm) {
            this.setSolenoid(DoubleSolenoid.Value.kForward);
            extensionStartTime = Clock.currentTimeMillis();
        }
    }

    /**
     * Lower arm, but only if it is enabled
     */
    @Override
    public void lower() {
        System.out.println(getLogPrefix(this) + "lower");

        if (enableArm) {
            setSolenoid(DoubleSolenoid.Value.kReverse);
        }
    }

    @Override
    public void off() {
        System.out.println(getLogPrefix(this) + "off");

        setSolenoid(DoubleSolenoid.Value.kOff);
        turnMotorOff();
    }

    @Override
    public void setSolenoid(@NotNull final DoubleSolenoid.Value value) {
        solenoidSubsystem.setSolenoid(value);
        if(value == DoubleSolenoid.Value.kForward) {
            armIsExtending = true;
        } else if(value == DoubleSolenoid.Value.kReverse) {
            armIsExtending = false;
        }

        reallySure = false;
    }

    @Override
    public @NotNull DoubleSolenoid.Value getSolenoidPosition() {
        return solenoidSubsystem.getSolenoidPosition();
    }

    /**
     * Move the winch if the arm is up.
     * Has to be called twice (double button
     * press) for it to work (I think?)
     */
    @Override
    public void turnMotorOn() {
        if (armIsUp()) {
            if (!reallySure) {
                reallySure = true;
            } else {
                setSolenoid(DoubleSolenoid.Value.kReverse);
                motorSubsystem.turnMotorOn();
                enableArm = false;
            }
        }
    }

    @Log
    private boolean armIsUp() {
        if (!armIsExtending) {
            return false;
        }
        return Clock.currentTimeMillis() >= extensionStartTime + extensionTimeMillis;
    }

    /**
     * Turn off the winch
     */
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
