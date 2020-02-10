package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.usfirst.frc.team449.robot.mixIn.*;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleUnaryOperator;

/**
 * A Jackson {@link com.fasterxml.jackson.databind.Module} for adding mix-in annotations to classes.
 */
public class WPIModule extends SimpleModule {

    /**
     * Default constructor
     */
    public WPIModule() {
        super("WPIModule");
    }

    /**
     * Mixes in some mix-ins to the given context.
     *
     * @param context the context to set up
     */
    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(DoubleUnaryOperator.class, DoubleUnaryOperatorMixIn.class);
        context.setMixInAnnotations(BooleanSupplier.class, BooleanSupplierMixIn.class);

        context.setMixInAnnotations(Subsystem.class, SubsystemMixIn.class);

        context.setMixInAnnotations(Command.class, CommandMixIn.class);
        context.setMixInAnnotations(WaitCommand.class, WaitCommandMixIn.class);
        context.setMixInAnnotations(ConditionalCommand.class, ConditionalCommandMixIn.class);
    }
}
