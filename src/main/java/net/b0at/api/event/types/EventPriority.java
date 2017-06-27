package net.b0at.api.event.types;

/**
 * Represents the priority of an Event.
 * Events are called in the order of HIGHEST to LOWEST EventPriority.
 */
public enum EventPriority {
    HIGHEST(100), HIGH(10), MEDIUM(0), LOW(-10), LOWEST(-100);

    private int priority;

    EventPriority(final int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
