package org.usfirst.frc.team449.robot.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.usfirst.frc.team449.robot.generalInterfaces.rumbleable.Rumbleable;
import org.usfirst.frc.team449.robot.jacksonWrappers.FPSTalon;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;

import javax.annotation.Nullable;


@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class TalonRumbleComponent implements MappedRunnable {

    FPSTalon talon;
    Rumbleable joystick;
    boolean inverted;
    Double rumbleAmount;

    @JsonCreator
    public TalonRumbleComponent(@JsonProperty(required = true) FPSTalon talon,
                                @JsonProperty(required = true) Rumbleable joystick,
                                @Nullable Double rumbleAmount,
                                boolean inverted) {
        this.talon = talon;
        this.joystick = joystick;
        this.inverted = inverted;
        this.rumbleAmount = rumbleAmount != null ? rumbleAmount : 1;
    }

    @Override
    public void run() {
        if (talon.isInhibitedForward() ^ inverted) {
            joystick.rumble(0, rumbleAmount);
        } else if (talon.isInhibitedReverse() ^ inverted) {
            joystick.rumble(rumbleAmount, 0);
        } else {
            joystick.rumble(0, 0);
        }
    }
}