package org.mobicents.protocols.ss7.map.load;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CsvWriter {

    private final String name;

    private Map<String, Counter> counters = new LinkedHashMap<>();

    private ScheduledExecutorService printExecutor = Executors.newScheduledThreadPool(1);

    public CsvWriter(String name) {
        this.name = name;
    }

    public void start(int initialDelay, int writerThreadPeriod) throws FileNotFoundException, UnsupportedEncodingException {
        CsvTask csvTask = new CsvTask(this);
        printExecutor.scheduleAtFixedRate(csvTask, initialDelay, writerThreadPeriod, TimeUnit.MILLISECONDS);
    }

    public void stop(int terminationDelay) throws InterruptedException {
        Thread.sleep(terminationDelay);
        printExecutor.shutdown();
        boolean isTerminated = printExecutor.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS);
        if (!isTerminated)
            printExecutor.shutdownNow();
    }

    public void addCounter(String name) {
        Counter counter = new Counter(name);
        counters.put(name, counter);
    }

    public void incrementCounter(String name) {
        Counter counter = counters.get(name);
        if (counter != null) {
            counter.incrementAndGet();
        }
    }

    public String name() {
        return name;
    }

    public List<Counter> getCounters() {
        return new ArrayList<>(counters.values());
    }

}
