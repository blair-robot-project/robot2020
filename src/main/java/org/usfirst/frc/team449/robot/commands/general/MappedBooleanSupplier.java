package org.usfirst.frc.team449.robot.commands.general;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.function.BooleanSupplier;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public interface MappedBooleanSupplier extends BooleanSupplier {
}
