package net.b0at.api.event;

import net.b0at.api.event.types.EventPriority;
import net.b0at.api.event.types.EventTiming;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    EventPriority priority() default EventPriority.MEDIUM;
    EventTiming timing() default EventTiming.PRE;
    boolean persistent() default false;
}
