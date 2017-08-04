package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by piotr.sokolowski on 2017-06-02.
 */
public class LocalTimerFacility implements ITimerFacility {

    private TCAPProviderImpl provider;

    public LocalTimerFacility(TCAPProviderImpl provider) {
        this.provider=provider;
    }


    @Override
    public Object schedule(final ITimerTask task, final long time, final TimeUnit units) throws IllegalArgumentException {
        return provider.getLocalExecutor().schedule(new Runnable() {
            public void run() {
                task.handleTimeEvent(provider);
            }
        },time,units);
    }

    @Override
    public void cancel(Object id) {
        ((ScheduledFuture<?>)id).cancel(false);
    }
}
