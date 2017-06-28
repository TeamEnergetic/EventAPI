EventAPI
========

This is the event API that the Energetic client - and a lot of our other projects - use. It is designed to be somewhat similar to Bukkit's event API.

We like to think that it is reasonably good, stable, and fast, but if you have any improvements, feel free to submit a pull request.

You're free to use this in your own project as well, following the terms of the license - let us know if you do!

## Documentation

Here are some simple examples. For more advanced usage, consult the code.

```java
public class EventAPIExample {
    private final EventManager eventManager = new EventManager();
    
    public void registerEvents() {
        this.eventManager.registerListener(new ListenerExample());
    }
    
    public void callSomeEvents() {
        this.eventManager.fireEvent(new MyEvent());
    }
}

public class ListenerExample implements Listener {
    // Handle an event
    @EventHandler
    public void onMyEvent(MyEvent evt) {
        evt.setCancelled(true);
    }
}

public class MyEvent extends Event {
    
}
```


![lgpl logo](https://www.gnu.org/graphics/lgplv3-147x51.png)
