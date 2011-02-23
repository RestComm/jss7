package org.mobicents.protocols.ss7.m3ua.impl.scheduler;

import javolution.util.FastList;

import org.apache.log4j.Logger;

public class M3UAScheduler {
    protected static final Logger logger = Logger.getLogger(M3UAScheduler.class);
    protected FastList<M3UATask> tasks = new FastList<M3UATask>();

    public void execute(M3UATask task) {
        this.tasks.add(task);
    }

    public void tick() {
        long now = System.currentTimeMillis();
        for (FastList.Node<M3UATask> n = tasks.head(), end = tasks.tail(); (n = n.getNext()) != end;) {
            M3UATask task = n.getValue();
            // check if has been canceled from different thread.
            if (task.canceled) {
                tasks.remove(task);
            } else {

                try {
                    task.run(now);
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failuer on task run.", e);
                    }
                }
                // check if its canceled after run;
                if (task.canceled) {
                    tasks.remove(task);
                }
            }
            // tempTask = null;
        }
    }
}
