package net.b0at.api.event;

/**
 * Represents an Event that can be called using an EventManager.
 * All Events are cancellable, but it is up to the caller of the event to decide if they
 * want to do anything when the Event is cancelled.
 */
public class Event {
    private boolean cancelled;

    /**
     * @return boolean if the the Event is cancelled
     */
    public final boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @param cancelled
     *            is the Event cancelled
     */
    public final void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
