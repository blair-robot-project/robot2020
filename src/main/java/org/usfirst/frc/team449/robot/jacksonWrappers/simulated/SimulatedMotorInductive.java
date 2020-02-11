package org.usfirst.frc.team449.robot.jacksonWrappers.simulated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A simulated motor with both armature reaction and generator back-EMF.
 * <p>
 * The voltage source is assumed to be ideal and resistanceless.
 * </p>
 * <p>
 * Native angle unit is rotations. Other units are SI.
 * </p>
 */
public class SimulatedMotorInductive extends SimulatedMotorSimple {
    // Default motor parameters.
    public static class DefaultConstants extends SimulatedMotorSimple.DefaultConstants {
        protected DefaultConstants() {
        }

        /**
         * (V / (rev/s)) K_e - voltage per RPS due to back EMF.
         */
        public static final double K_e = 0;
        /**
         * (H) Armature inductance
         */
        public static final double INDUCTANCE = 0;
    }

    @NotNull
    private final double backEmfConstant;
    @NotNull
    private final double armatureInductance;

    /**
     * Constructs a new motor with the specified parameters.
     *
     * @param moment             (kg * m^2) - moment of inertia of motor and load.
     * @param torqueConstant     K_t (but also called K_m for some reason) (N*m / A) - motor torque constant in torque over armature current.
     * @param backEmfConstant    K_e (V / (rev/s))
     * @param frictionCoeff      (N*m / (rev/s)) - friction coefficient in torque over angular velocity (should be negative)
     * @param armatureResistance R_a (Ω) - armature resistance
     * @param armatureInductance (H)
     */
    public SimulatedMotorInductive(@Nullable final Double moment,
                                   @Nullable final Double torqueConstant,
                                   @Nullable final Double backEmfConstant,
                                   @Nullable final Double frictionCoeff,
                                   @Nullable final Double armatureResistance,
                                   @Nullable final Double armatureInductance) {
        super(moment, torqueConstant, frictionCoeff, armatureResistance);

        this.backEmfConstant = Objects.requireNonNullElse(backEmfConstant, DefaultConstants.K_e);
        this.armatureInductance = Objects.requireNonNullElse(armatureInductance, DefaultConstants.INDUCTANCE);
    }

    @Override
    public void updatePhysics(final double deltaSecs, final double voltage, final double loadTorque) {
        // TODO Figure out armature reaction and what it has to do with these.
        final double resistiveVoltage = this.current * this.resistance;
        final double backEmf = this.backEmfConstant * this.velocity;

        final double netVoltage = voltage - resistiveVoltage - backEmf;
        final double deltaCurrent = netVoltage / this.armatureInductance;

        final double frictionTorque = this.frictionCoeff * this.velocity;
        final double motorTorque = this.torqueCoeff * this.current;
        final double netTorque = motorTorque + frictionTorque + loadTorque;
        final double angularAcceleration = netTorque / this.moment;

        this.position += this.velocity * deltaSecs;
        this.velocity += angularAcceleration * deltaSecs;
        this.current += deltaCurrent * deltaSecs;
    }
}
