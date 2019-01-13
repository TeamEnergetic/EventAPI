package net.b0at.api.event.util;

import net.b0at.api.event.Event;

public class DummyEvent extends Event {
    private int value;

    public DummyEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
