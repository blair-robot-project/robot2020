package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class MappedGenericTest<T> {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public MappedGenericTest(@NotNull @JsonProperty(required = true) final T value){
        System.err.println(value.getClass());
    }
}
