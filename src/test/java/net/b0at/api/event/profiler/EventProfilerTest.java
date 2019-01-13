package net.b0at.api.event.profiler;

import net.b0at.api.event.Event;
import net.b0at.api.event.EventHandler;
import net.b0at.api.event.EventManager;
import net.b0at.api.event.cache.HandlerEncapsulator;
import net.b0at.api.event.types.EventTiming;
import net.b0at.api.event.util.DummyEvent;
import net.b0at.api.event.util.DummyEvent2;
import org.junit.Assert;
import org.junit.Test;

import java.util.NavigableSet;

public class EventProfilerTest {

    @Test
    public void testPostFireEvent() {
        // TODO: Improve this test
        EventManager<Event> manager = new EventManager<>(Event.class);
        final int[] count = { 0 };

        IEventProfiler<Event> profiler = new IEventProfiler<Event>() {
            @Override
            public void postFireEvent(Event event, EventTiming timing, NavigableSet<HandlerEncapsulator<Event>> handlers) {
                count[0]++;
            }
        };

        manager.setEventProfiler(profiler);

        manager.registerListener(new Object() {
            @EventHandler
            public void onDummyEvent(DummyEvent dummyEvent) { }
        });

        manager.registerListener(new Object() {
            @EventHandler
            public void onDummyEvent2(DummyEvent2 dummyEvent2) { }
        });

        manager.fireEvent(new DummyEvent(0));
        manager.fireEvent(new DummyEvent2());

        Assert.assertEquals(2, count[0]);
    }

    // TODO: Add more tests
}
