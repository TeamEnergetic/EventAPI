package net.b0at.api.event;

import net.b0at.api.event.exceptions.ListenerAlreadyRegisteredException;
import net.b0at.api.event.exceptions.ListenerNotAlreadyRegisteredException;
import net.b0at.api.event.types.EventPriority;
import net.b0at.api.event.types.EventTiming;
import net.b0at.api.event.util.DummyEvent;
import net.b0at.api.event.util.DummyListener;
import org.junit.Assert;
import org.junit.Test;

public class EventManagerTest {
    @Test
    public void testRegisterListener() {
        EventManager<Event> manager = new EventManager<>(Event.class);

        manager.registerListener(new DummyListener((dummyEvent) -> {}));

        Assert.assertEquals(1, manager.getRegisteredListenerCount());
    }

    @Test
    public void testDeregisterListener() {
        EventManager<Event> manager = new EventManager<>(Event.class);

        DummyListener listener = new DummyListener((dummyEvent) -> {});

        manager.registerListener(listener);
        manager.deregisterListener(listener);

        Assert.assertEquals(0, manager.getRegisteredListenerCount());
    }

    @Test
    public void testDeregisterAll() {
        EventManager<Event> manager = new EventManager<>(Event.class);

        DummyListener listener1 = new DummyListener((dummyEvent) -> {});
        DummyListener listener2 = new DummyListener((dummyEvent) -> {});

        manager.registerListener(listener1);
        manager.registerListener(listener2);

        manager.deregisterAll();

        Assert.assertEquals(0, manager.getRegisteredListenerCount());
    }

    @Test
    public void testGetRegisteredListenerCount() {
        EventManager<Event> manager = new EventManager<>(Event.class);

        DummyListener listener1 = new DummyListener((dummyEvent) -> {});
        DummyListener listener2 = new DummyListener((dummyEvent) -> {});

        Assert.assertEquals(0, manager.getRegisteredListenerCount());

        manager.registerListener(listener1);
        Assert.assertEquals(1, manager.getRegisteredListenerCount());

        manager.registerListener(listener2);
        Assert.assertEquals(2, manager.getRegisteredListenerCount());
    }

    @Test
    public void testFireEvent() {
        EventManager<Event> manager = new EventManager<>(Event.class);
        final int[] count = { 0 };

        manager.registerListener(new DummyListener((dummyEvent) -> count[0]++));
        manager.fireEvent(new DummyEvent(0));

        Assert.assertEquals(1, count[0]);
    }

    @Test
    public void testConcurrency() {
        EventManager<Event> manager = new EventManager<>(Event.class);
        final int[] count = { 0 };

        DummyListener secondaryListener = new DummyListener((dummyEvent) -> {
            count[0]++;
        });

        manager.registerListener(new DummyListener((dummyEvent) -> {
            if (dummyEvent.getValue() == 0) {
                manager.registerListener(secondaryListener);
            } else {
                count[0]++;
            }
        }));
        // this event causes 'secondaryListener' to be registered
        manager.fireEvent(new DummyEvent(0));

        // this event counts how many times a dummy event is counted
        manager.fireEvent(new DummyEvent(1));

        Assert.assertEquals(2, count[0]);
    }

    @Test
    public void testAvoidDuplicates() {
        EventManager<Event> manager = new EventManager<>(Event.class);

        Object listener = new Object() { };

        manager.registerListener(listener);

        // register listener again
        Assert.assertThrows(ListenerAlreadyRegisteredException.class, () -> manager.registerListener(listener));
    }

    @Test
    public void testRemovingNonListener() {
        EventManager<Event> manager = new EventManager<>(Event.class);

        Object listener = new Object() { };

        Assert.assertThrows(ListenerNotAlreadyRegisteredException.class, () -> manager.deregisterListener(listener));
    }

    @Test
    public void testPriority() {
        EventManager<Event> manager = new EventManager<>(Event.class);
        final int[] value = { 0 };

        Object highestListener = new Object() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void onEveryEvent(DummyEvent event) {
                value[0] = 1;
            }
        };

        Object mediumListener = new Object() {
            @EventHandler(priority = EventPriority.MEDIUM)
            public void onDummyEvent(DummyEvent event) {
                value[0] = 2;
            }
        };

        Object lowestListener = new Object() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onDummyEvent(DummyEvent event) {
                value[0] = 3;
            }
        };

        manager.registerListener(mediumListener);
        manager.registerListener(highestListener);
        manager.fireEvent(new DummyEvent(0));

        Assert.assertEquals(2, value[0]);


        manager.registerListener(lowestListener);
        manager.fireEvent(new DummyEvent(0));

        Assert.assertEquals(3, value[0]);
    }

    @Test
    public void testMultiTiming() {
        EventManager<Event> manager = new EventManager<>(Event.class);
        DummyEvent event = new DummyEvent(0);
        EventTiming[] value = { null };

        manager.registerListener(new Object() {
            @EventHandler
            public void onEventWithTiming(DummyEvent event, EventTiming timing) {
                value[0] = timing;
            }
        });

        Assert.assertNull(value[0]);

        manager.fireEvent(event, EventTiming.PRE);
        Assert.assertEquals(EventTiming.PRE, value[0]);

        manager.fireEvent(event, EventTiming.POST);
        Assert.assertEquals(EventTiming.POST, value[0]);
    }
}
