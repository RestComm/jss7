package org.mobicents.protocols.ss7.map.load.mapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;

public class MAPpContext {
    MAPpState currentState = MAPpState.NULL;
    
    private HashMap<String, AtomicLong> counters = new HashMap();
    int NDIALOGS = 50000;
    int MAXCONCURRENTDIALOGS = 15;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(40);

    MAPScenario scenario;
    Logger logger = Logger.getLogger("mapp.msglog");
    Properties props = new Properties();
    Map<String, Object> data = new HashMap();

    void switchToState (MAPpState newState){
        currentState = newState;
    }

    public MAPpState getCurrentState() {
        return currentState;
    }
    
    
    
    synchronized void incrementCounter(String counterName) {
        AtomicLong counter = counters.get(counterName);
        if (counter == null) {
            counter = new AtomicLong(0);
            AtomicLong prevCounter = counters.put(counterName, counter);
            if (prevCounter != null) {
                counter = prevCounter;
            }
        }
        counter.incrementAndGet();
    }
    
    AtomicLong getCurrentCounter(String counterName) {
        return counters.get(counterName);
    }

    public synchronized Map<String, AtomicLong> retrieveAndResetCurrentCounters() {
        HashMap currentCounters = counters;
        counters = new HashMap<>();
        return currentCounters;
    }
}
