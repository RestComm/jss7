package org.mobicents.protocols.ss7.m3ua.impl.scheduler;

public abstract class M3UATask {

    protected boolean canceled = false;
    protected int index;
    protected M3UAScheduler scheduler;

    public void run(long now) {
        if (!canceled) {
            // exception in caught in scheduler.
            tick(now);
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public abstract void tick(long now);

    public void cancel() {
        this.canceled = true;
        // dont do this, let it be lazely reclaimed if ever, this causes race!
        // remove task from list
        // if (scheduler != null && (index >=0) && (index <
        // scheduler.tasks.length)) {
        // scheduler.tasks[index] = null;
        // }
    }
}
