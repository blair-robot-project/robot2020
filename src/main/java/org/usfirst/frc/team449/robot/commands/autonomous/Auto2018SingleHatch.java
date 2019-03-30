package org.usfirst.frc.team449.robot.commands.autonomous;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.commands.general.ConditionalCommandDigitalInputBased;
import org.usfirst.frc.team449.robot.commands.limelight.SetTracking;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.commands.SetHeading;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class Auto2018SingleHatch<T extends Subsystem & SubsystemAHRS & SubsystemMPManualTwoSides> extends CommandGroup {

    /**
     * Default constructor.
     *
     * @param adjustCommand The command to run limelight auto-adjustment. Can be null to not auto-adjust.
     * @param drive The drive to move the robot to the cargo bay.
     * @param hatchMech The hatch mechanism that holds the hatch.
     * @param angularCompliance The subsystem controlling the pistons that push the hatch mech forward and back.
     * @param leftDriveCommand The command to drive to the cargo bay when the robot is on the left side.
     * @param rightDriveCommand The command to drive to the cargo bay when the robot is on the right side.
     * @param startingSideSwitch A digitalInput that's true when the robot is on the left side.
     * @param driveDefaultCommand The command that lets the driver control the robot.
     * @param compressor The compressor, which will be turned off for auto. Can be null to not turn off a compressor.
     */
    @JsonCreator
    public Auto2018SingleHatch(@Nullable Command adjustCommand,
                               @NotNull @JsonProperty(required = true) T drive,
                               @NotNull @JsonProperty(required = true) SubsystemSolenoid hatchMech,
                               @NotNull @JsonProperty(required = true) SubsystemSolenoid angularCompliance,
                               @NotNull @JsonProperty(required = true) Command leftDriveCommand,
                               @NotNull @JsonProperty(required = true) Command rightDriveCommand,
                               @NotNull @JsonProperty(required = true) MappedDigitalInput startingSideSwitch,
                               @NotNull @JsonProperty(required = true) Command driveDefaultCommand,
                               @Nullable Pneumatics compressor,
                               boolean useLimelight) {
        if (compressor != null) {
            addParallel(new StopCompressor(compressor));
        }
        if (adjustCommand != null) {
            addParallel(adjustCommand);
        }
        if (useLimelight) {
            addParallel(new SetTracking(true));
        }

        addParallel(new SolenoidForward(angularCompliance));
        addParallel(new SolenoidForward(hatchMech));

        addSequential(new SetHeading(drive, 0));
        addSequential(new ConditionalCommandDigitalInputBased(leftDriveCommand, rightDriveCommand, startingSideSwitch));
        addSequential(new SolenoidReverse(hatchMech));
        if (compressor != null) {
            addSequential(new StartCompressor(compressor));
        }
        addSequential(new SetTracking(false));
        addSequential(driveDefaultCommand);
    }
}
