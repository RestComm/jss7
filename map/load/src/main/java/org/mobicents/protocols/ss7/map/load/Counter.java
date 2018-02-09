package org.mobicents.protocols.ss7.map.load;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {

    private final String name;
    private AtomicLong counter;

    public Counter(String name) {
        this.name = name;
        this.counter = new AtomicLong();
    }

    public long incrementAndGet() {
        return this.counter.incrementAndGet();
    }

    public long get() {
        return this.counter.get();
    }

    public String getName() {
        return this.name;
    }
}
