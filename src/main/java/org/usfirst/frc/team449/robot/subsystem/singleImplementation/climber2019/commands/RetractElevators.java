package org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.drive.DriveSubsystem;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.climber2019.SubsystemClimber2019;

public class RetractElevators<T extends Subsystem & DriveSubsystem> extends CommandGroup {

    @JsonCreator
    public RetractElevators(@JsonProperty(required = true) @NotNull SubsystemClimber2019 climber,
                            @JsonProperty(required = true) @NotNull T drive,
                            @JsonProperty(required = true) double maxVelRetract,
                            @JsonProperty(required = true) double maxAccelRetract,
                            @Nullable Double unstickTolerance) {
        requires(climber);

        StopClimb stopClimb = new StopClimb<>(climber, drive);
        RunElevator retractLegs = new RunElevator(RunElevator.MoveType.BOTH, maxVelRetract, maxAccelRetract,
                null, 0, 0, 0, 0, unstickTolerance, climber);

        addSequential(stopClimb);
        addSequential(retractLegs);
    }

}
