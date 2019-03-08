package org.usfirst.frc.team449.robot.commands.general;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.other.MotionProfileData;

import java.util.function.Supplier;

/**
 * Prints out a supplied motion profile. For debugging only so not constructable via Jackson.
 */
public class PrintProfile extends InstantCommand {

    /**
     * The supplier of the motion profiles to print.
     */
    @NotNull
    private final Supplier<MotionProfileData> left, right;

    /**
     * Default constructor
     *
     * @param left  The supplier of the left profile.
     * @param right The supplier of the right profile.
     */
    public PrintProfile(@NotNull Supplier<MotionProfileData> left, @NotNull Supplier<MotionProfileData> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Print the profiles.
     */
    public void execute() {
        MotionProfileData leftP = left.get(), rightP = right.get();
        System.out.println("Left: ");
        if (leftP.getData().length == 0){
            System.out.println("Empty");
        }
        for (double[] line : leftP.getData()) {
            System.out.println("Position: " + line[0] + ", velocity: " + line[1] + ", acceleration: " + line[2]);
        }
        System.out.println("Right: ");
        if (leftP.getData().length == 0){
            System.out.println("Empty");
        }
        for (double[] line : rightP.getData()) {
            System.out.println("Position: " + line[0] + ", velocity: " + line[1] + ", acceleration: " + line[2]);
        }
    }

    /**
     * Log on end.
     */
    public void end() {
        Shuffleboard.addEventMarker("PrintProfile end", this.getClass().getSimpleName(), EventImportance.kNormal);
    }
}
