package org.usfirst.frc.team449.robot.other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.generalInterfaces.updatable.Updatable;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * A Runnable for updating cached variables.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class Updater implements MappedRunnable, Updatable {

    /**
     * The objects to update.
     */
    @NotNull
    private final List<Updatable> updatables;

    /**
     * Default constructor
     *
     * @param updatables The objects to update.
     */
    public Updater(@NotNull @JsonProperty(required = true) final List<Updatable> updatables) {
        this.updatables = updatables;
    }

    /**
     * Update all the updatables.
     */
    @Override
    public void run() {
        for (final Updatable updatable : this.updatables) {
            updatable.update();
        }
    }

    private static final Updater defaultInstance = new Updater(new ArrayList<>());

    /**
     * Subscribes the specified updatable to being updated.
     */
    public static void subscribe(final Updatable updatable) {
        defaultInstance.updatables.add(updatable);
    }

    /**
     * Injects the default updatable instance into the specified list of updatables.
     *
     * @param updatables The objects to update.
     */
    @JsonCreator
    public static Updater create(@NotNull @JsonProperty(required = true) final List<Updatable> updatables) {
        final var listWithDefaultInstance = new ArrayList<Updatable>(updatables.size() + 1);
        listWithDefaultInstance.addAll(updatables);
        listWithDefaultInstance.add(defaultInstance);
        return new Updater(listWithDefaultInstance);
    }

    /**
     * Updates all cached values with current ones.
     */
    @Override
    public void update() {
        this.run();
    }
}
