package net.b0at.api.event;

import net.b0at.api.event.types.EventEncapsulator;
import net.b0at.api.event.types.EventPriority;
import net.b0at.api.event.types.EventTiming;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
    private List<EventEncapsulator> eventEncapsulatorList = new CopyOnWriteArrayList<>();
    private boolean needsSorting = true;

    /**
     * Register a new Listener with this EventManager.
     * @param listener The Listener to register.
     * @param onlyAddPersistent If true, only persistent EventHandler methods will be registered.
     */
    @SuppressWarnings("unchecked")
    public void registerListener(Listener listener, boolean onlyAddPersistent) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length == 1 && method.isAnnotationPresent(EventHandler.class)) {
                Class<? extends Event> param = (Class<? extends Event>) method.getParameterTypes()[0];
                EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                if (onlyAddPersistent) {
                    if (eventHandler.persistent()) {
                        this.eventEncapsulatorList.add(new EventEncapsulator<>(listener, param, eventHandler.timing(), eventHandler.priority(), method));
                    }
                } else {
                    this.eventEncapsulatorList.add(new EventEncapsulator<>(listener, param, eventHandler.timing(), eventHandler.priority(), method));
                }
            }
        }

        this.needsSorting = true;
    }

    /**
     * Register a new Listener with this EventManager.
     * This will register all EventHandler methods in the Listener class.
     * @param listener The Listener to register.
     */
    public void registerListener(Listener listener) {
        this.registerListener(listener, false);
    }

    /**
     * Deregister a Listener from this EventManager.
     * @param listener Listener to deregister.
     */
    public void deregisterListener(Listener listener) {
        List<EventEncapsulator<?>> toRemove = new ArrayList<>();

        for (EventEncapsulator<?> eventEncapsulator : this.eventEncapsulatorList) {
            if (eventEncapsulator.getListener().getClass() == listener.getClass()) {
                toRemove.add(eventEncapsulator);
            }
        }

        this.eventEncapsulatorList.removeAll(toRemove);
    }

    /**
     * Deregister all Listeners from this EventManager.
     */
    public void deregisterAll() {
        this.eventEncapsulatorList.clear();
    }

    /**
     * Get the number of Listeners registered with this EventManager.
     * @return Number of registered Listeners.
     */
    public int getRegisteredListenerCount() {
        return this.eventEncapsulatorList.size();
    }

    /**
     * Fire an Event, with a default timing of PRE.
     * @param event The Event to fire.
     */
    public void fireEvent(Event event) {
        this.fireEvent(event, EventTiming.PRE);
    }

    /**
     * Fire an Event with the given EventTiming.
     * @param event Event to fire.
     * @param timing EventTiming to fire the Event with.
     */
    public synchronized void fireEvent(Event event, EventTiming timing) {
        if (this.needsSorting) {
            this.sortEncapsulatorList();
            this.needsSorting = false;
        }

        for (EventEncapsulator<?> eventEncapsulator : this.eventEncapsulatorList) {
            if (eventEncapsulator.shouldInvoke(timing, event)) {
                eventEncapsulator.invoke(event);
            }
        }
    }

    private void sortEncapsulatorList() {
        EventEncapsulator<?>[] sorted = new EventEncapsulator[this.eventEncapsulatorList.size()];
        int index = 0;

        for (EventPriority priority : EventPriority.values()) {
            for (EventEncapsulator<?> encapsulator : this.eventEncapsulatorList) {
                if (encapsulator.getPriority() == priority) {
                    sorted[index++] = encapsulator;
                }
            }
        }

        this.eventEncapsulatorList = new CopyOnWriteArrayList<>(sorted);
    }
}
