package net.b0at.api.event.types;

import net.b0at.api.event.EventHandler;

/**
 * Represents the priority of an {@link EventHandler}.
 *
 * <p>
 * {@link EventHandler}s are invoked in the ascending {@link #ordinal()} order, that is:
 * starting from {@link #HIGHEST} and ending with {@link #MONITOR}.
 * </p>
 */
public enum EventPriority {
    /**
     * {@link EventHandler}s with this {@link EventPriority} have the FIRST (1) priority to be invoked.
     */
    HIGHEST,

    /**
     * {@link EventHandler}s with this {@link EventPriority} have the SECOND (2) priority to be invoked.
     */
    HIGHER,

    /**
     * {@link EventHandler}s with this {@link EventPriority} have the THIRD (3) priority to be invoked.
     */
    HIGH,

    /**
     * {@link EventHandler}s with this {@link EventPriority} have the FOURTH (4) priority to be invoked.
     * <p>
     * This is the default {@link EventPriority} for {@link EventHandler}s
     * </p>
     */
    MEDIUM,

    /**
     * {@link EventHandler}s with this {@link EventPriority} have the FIFTH (5) priority to be invoked.
     */
    LOW,

    /**
     * {@link EventHandler}s with this {@link EventPriority} have the SIXTH (6) priority to be invoked.
     */
    LOWER,

    /**
     *{@link EventHandler}s with this {@link EventPriority} have the SEVENTH (7) priority to be invoked.
     */
    LOWEST,

    /**
     * {@link EventHandler}s with this {@link EventPriority} have the EIGHTH (8), and LAST priority to be invoked.
     *
     * <p>
     * By convention, all {@link EventHandler}s with this {@link EventPriority} shall not modify events in any way.
     * </p>
     */
    MONITOR
}
