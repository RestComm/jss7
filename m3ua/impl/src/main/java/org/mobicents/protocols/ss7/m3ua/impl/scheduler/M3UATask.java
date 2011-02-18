package org.mobicents.protocols.ss7.m3ua.impl.scheduler;


public abstract class M3UATask implements Runnable {

    protected boolean canceled = false;
    protected long deadLine;
    protected int index;
    protected M3UAScheduler scheduler;

    public void run() {
        if (!canceled) {
            try {
                // exception in caught in scheduler.
                perform();
            } finally {
                canceled = true;
            }
        }

    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public abstract void perform();

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
