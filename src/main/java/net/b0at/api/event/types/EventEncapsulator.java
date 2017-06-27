package net.b0at.api.event.types;


import net.b0at.api.event.Event;
import net.b0at.api.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class EventEncapsulator<T> {
    private Listener listener;
    private Class<? extends Event> event;
    private Method method;
    private EventTiming timing;
    private EventPriority priority;
    private boolean everyEvent;

    public EventEncapsulator(Listener listener, Class<? extends Event> event, EventTiming timing, EventPriority priority, Method method) {
        if (method != null) {
            method.setAccessible(true);
        }
        this.listener = listener;
        this.event = event;
        this.timing = timing;
        this.priority = priority;
        this.method = method;
        this.everyEvent = this.event != null && this.event.equals(Event.class);
    }

    public Listener getListener() {
        return this.listener;
    }

    public EventPriority getPriority() {
        return this.priority;
    }

    public void invoke(Event event) {
        try {
            this.method.invoke(this.listener, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean shouldInvoke(EventTiming timing, Event event) {
        return (this.timing == timing) && ((this.everyEvent) || (event.getClass() == this.event));
    }

    @Override
    public String toString() {
        return String.format("%s.%s(%s)", this.listener.getClass().getCanonicalName(), this.method.getName(), this.event.getName());
    }
}
