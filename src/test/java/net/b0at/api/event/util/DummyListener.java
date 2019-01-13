package net.b0at.api.event.util;

import net.b0at.api.event.EventHandler;

import java.util.function.Consumer;

public class DummyListener {
    private Consumer<DummyEvent> consumer;

    public DummyListener(Consumer<DummyEvent> consumer) {
        this.consumer = consumer;
    }

    @EventHandler
    public void onDummyEvent(DummyEvent dummyEvent) {
        this.consumer.accept(dummyEvent);
    }
}
