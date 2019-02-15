package org.usfirst.frc.team449.robot.components;

import java.nio.DoubleBuffer;
import java.util.function.DoubleSupplier;

public class LimeLightYawTargetComponent implements DoubleSupplier {
    DoubleBuffer buffer;
    LimeLightComponent limeLightComponent;

    public LimeLightYawTargetComponent(int bufferCapacity, LimeLightComponent limeLightComponent) {
        buffer = DoubleBuffer.allocate(bufferCapacity);
        this.limeLightComponent = limeLightComponent;
    }

    @Override
    public double getAsDouble() {
        buffer.put(limeLightComponent.getAsDouble());
        double[] bufferArray = buffer.array();
        double arraySum = 0;
        for (int i = 0; i < bufferArray.length; i++){
            arraySum +=  bufferArray[i];
        }
        return arraySum/bufferArray.length;
    }
}
