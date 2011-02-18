package org.mobicents.protocols.ss7.m3ua.impl.scheduler;

import org.apache.log4j.Logger;

public class M3UAScheduler {
    protected static final Logger logger = Logger
            .getLogger(M3UAScheduler.class);
    protected M3UATask[] tasks = new M3UATask[5];

    M3UATask tempTask = null;

    public void schedule(M3UATask task, int delay) {
        task.deadLine = System.currentTimeMillis() + delay;
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] == null || tasks[i].canceled) {
                task.canceled = false;
                tasks[i] = task;
                task.index = i;
                task.scheduler = this;
                break;
            }
        }
    }

    public void tick() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] != null && (tasks[i].deadLine <= now)) {
                // check if has been canceled from different thread.
                if (tasks[i].canceled) {
                    tasks[i] = null;
                } else {
                    tempTask = tasks[i];
                    try {
                        tempTask.run();
                    } catch (Exception e) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Failuer on task run.", e);
                        }
                    }
                    // check if its canceled after run;
                    if (tasks[i].canceled) {
                        tasks[i] = null;
                    }
                }
                // tempTask = null;
            }
        }
    }
}
