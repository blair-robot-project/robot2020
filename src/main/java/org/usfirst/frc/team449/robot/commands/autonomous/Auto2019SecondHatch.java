package org.usfirst.frc.team449.robot.commands.autonomous;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.commands.general.ConditionalCommandDigitalInputBased;
import org.usfirst.frc.team449.robot.commands.limelight.SetTracking;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.TwoSideMPSubsystem.manual.SubsystemMPManualTwoSides;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.SubsystemSolenoid;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.commands.SolenoidForward;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.commands.SolenoidReverse;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.pneumatics.Pneumatics;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.pneumatics.commands.StartCompressor;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.pneumatics.commands.StopCompressor;

/**
 * Run a command to drive to the cargo bay and drop off a hatch, then give control back to the driver.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class Auto2019SecondHatch<T extends Subsystem & SubsystemAHRS & SubsystemMPManualTwoSides> extends CommandGroup {

    /**
     * Default constructor
     *
     * @param adjustCommand The command to run limelight auto-adjustment. Can be null to not auto-adjust.
     * @param hatchMech The hatch mechanism that holds the hatch.
     * @param startingSideSwitch A digitalInput that's true when the robot is on the left side.
     * @param compressor The compressor, which will be turned off for auto. Can be null to not turn off a compressor.
     * @param leftToLoadRevCommand The motion profile command going to the left loading station
     * @param rightToLoadRevCommand The motion profile command going to the right loading station
     * @param toLoadFwdCommand The motion profile going to loading station from second hatch location
     * @param leftLoadToFwdCommand The motion profile going to left front hatch from left loading station
     * @param rightLoadToFwdCommand The motion profile going to right front hatch from right loading station
     * @param leftLoadToRevCommand The reverse profile from the left loading station backwards
     * @param rightLoadToRevCommand The reverse profile from the right loading station backwards
     */
    @JsonCreator
    public Auto2019SecondHatch(@Nullable Command adjustCommand,
                               @NotNull @JsonProperty(required = true) SubsystemSolenoid hatchMech,
                               @NotNull @JsonProperty(required = true) Command leftToLoadRevCommand,
                               @NotNull @JsonProperty(required = true) Command rightToLoadRevCommand,
                               @NotNull @JsonProperty(required = true) Command toLoadFwdCommand,
                               @NotNull @JsonProperty(required = true) Command leftLoadToFwdCommand,
                               @NotNull @JsonProperty(required = true) Command rightLoadToFwdCommand,
                               @NotNull @JsonProperty(required = true) Command leftLoadToRevCommand,
                               @NotNull @JsonProperty(required = true) Command rightLoadToRevCommand,
                               @NotNull @JsonProperty(required = true) Command navXTurnToAngleCommand,
                               @NotNull @JsonProperty(required = true) MappedDigitalInput startingSideSwitch,
                               @Nullable Pneumatics compressor) {
        if (compressor != null) {
            addParallel(new StopCompressor(compressor));
        }
        if (adjustCommand != null) {
            addParallel(adjustCommand);
        }
        addParallel(new SetTracking(true));
        addSequential(new ConditionalCommandDigitalInputBased(leftToLoadRevCommand, rightToLoadRevCommand, startingSideSwitch));
        addSequential(navXTurnToAngleCommand);
        addSequential(toLoadFwdCommand);
        addSequential(new SolenoidForward(hatchMech));
        addSequential(new ConditionalCommandDigitalInputBased(leftLoadToRevCommand, rightLoadToRevCommand, startingSideSwitch));
        addSequential(new ConditionalCommandDigitalInputBased(leftLoadToFwdCommand, rightLoadToFwdCommand, startingSideSwitch));
        addSequential(new SolenoidReverse(hatchMech));
        if (compressor != null) {
            addSequential(new StartCompressor(compressor));
        }
    }
}