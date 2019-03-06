package org.usfirst.frc.team449.robot.commands.autonomous;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team449.robot.commands.general.ConditionalCommandDigitalInputBased;
import org.usfirst.frc.team449.robot.commands.limelight.SetTracking;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.SubsystemAHRS;
import org.usfirst.frc.team449.robot.subsystem.interfaces.AHRS.commands.SetHeading;
import org.usfirst.frc.team449.robot.subsystem.interfaces.motionProfile.TwoSideMPSubsystem.manual.SubsystemMPManualTwoSides;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.SubsystemSolenoid;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.commands.SolenoidForward;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.commands.SolenoidReverse;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class Auto2018SingleHatch<T extends Subsystem & SubsystemAHRS & SubsystemMPManualTwoSides> extends CommandGroup {

    @JsonCreator
    public Auto2018SingleHatch(Command adjustCommand,
                               T drive,
                               SubsystemSolenoid hatchMech,
                               SubsystemSolenoid angularCompliance,
                               Command leftDriveCommand,
                               Command rightDriveCommand,
                               MappedDigitalInput startingSideSwitch,
                               Command driveDefaultCommand){
        addParallel(adjustCommand);
        addParallel(new SetTracking(true));
        addParallel(new SolenoidForward(angularCompliance));
        addParallel(new SolenoidForward(hatchMech));
        addSequential(new SetHeading(drive, 0));
        addSequential(new ConditionalCommandDigitalInputBased(leftDriveCommand, rightDriveCommand, startingSideSwitch));
        addSequential(new SolenoidReverse(hatchMech));
        addSequential(driveDefaultCommand);
    }
}
