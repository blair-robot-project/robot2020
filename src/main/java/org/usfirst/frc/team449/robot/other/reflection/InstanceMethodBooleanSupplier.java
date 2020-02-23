package org.usfirst.frc.team449.robot.other.reflection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.usfirst.frc.team449.robot.commands.jacksonWrappers.BooleanSupplierBooleanSupplierBased;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;

/**
 * @param <T>
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class InstanceMethodBooleanSupplier<T> extends BooleanSupplierBooleanSupplierBased {
    @JsonCreator
    public InstanceMethodBooleanSupplier(@JsonProperty(required = true) final T source,
                                         @JsonProperty(required = true) final String methodName) throws NoSuchMethodException {
        super(getBooleanSupplier(source, methodName));
    }

    private static BooleanSupplier getBooleanSupplier(final Object source, final String methodName) throws NoSuchMethodException {
        final Method method = source.getClass().getMethod(methodName);

        final var actualReturnType = method.getGenericReturnType();
        if (!actualReturnType.equals(Boolean.class) && !actualReturnType.equals(boolean.class))
            throw new IllegalArgumentException();

        return () -> {
            try {
                return (Boolean) method.invoke(source);
            } catch (final IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
